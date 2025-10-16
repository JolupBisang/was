package com.jolupbisang.demo.infrastructure.audio;

import com.jolupbisang.demo.domain.audio.model.FullAudio;
import com.jolupbisang.demo.infrastructure.aws.s3.S3ClientUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Repository
@RequiredArgsConstructor
public class FullAudioRepositoryImpl implements FullAudioRepository {

    private final S3ClientUtil s3ClientUtil;

    private static final String S3_MEETING_KEY_PREFIX = "merged-audio/meeting-%d/";
    private static final String S3_AUDIO_META_EXTRACT_REGEX = "merged-audio/meeting-\\d+/user-(\\d+)/";
    private static final String S3_MEETING_USER_KEY_FORMAT = "merged-audio/meeting-%d/user-%d/merged.opus";

    private static final int USER_ID_REGEX_GROUP = 1;
    private static final int DEFAULT_PRESIGNED_URL_DURATION_DAY = 1;

    @Override
    public List<FullAudio> findAllByMeetingId(long meetingId) {
        String prefix = String.format(S3_MEETING_KEY_PREFIX, meetingId);
        List<String> objectKeys = s3ClientUtil.listObjectKeysByPrefix(prefix);

        Pattern userIdPattern = Pattern.compile(S3_AUDIO_META_EXTRACT_REGEX);

        List<Long> userIds = objectKeys.stream()
                .map(userIdPattern::matcher)
                .filter(Matcher::find)
                .map(matcher -> Long.parseLong(matcher.group(USER_ID_REGEX_GROUP)))
                .distinct()
                .toList();

        List<FullAudio> fullAudioList = new ArrayList<>();
        for (Long userId : userIds) {
            fullAudioList.add(findByMeetingIdAndUserId(meetingId, userId, Duration.ofDays(DEFAULT_PRESIGNED_URL_DURATION_DAY)));
        }

        return fullAudioList;
    }

    @Override
    public FullAudio findByMeetingIdAndUserId(long meetingId, long userId, Duration duration) {
        return new FullAudio(
                meetingId,
                userId,
                s3ClientUtil.generatePresignedUrl(
                        generateS3CompletedURLKey(meetingId, userId),
                        duration
                ));
    }

    private String generateS3CompletedURLKey(long meetingId, long userId) {
        return String.format(S3_MEETING_USER_KEY_FORMAT, meetingId, userId);
    }
}
