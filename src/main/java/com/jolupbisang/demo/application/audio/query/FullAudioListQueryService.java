package com.jolupbisang.demo.application.audio.query;

import com.jolupbisang.demo.application.audio.exception.AudioApplicationErrorCode;
import com.jolupbisang.demo.application.audio.query.dto.AudioListRes;
import com.jolupbisang.demo.domain.audio.model.FullAudio;
import com.jolupbisang.demo.domain.meeting.model.Meeting;
import com.jolupbisang.demo.global.exception.ApplicationException;
import com.jolupbisang.demo.global.exception.NotFoundException;
import com.jolupbisang.demo.infrastructure.audio.FullAudioRepository;
import com.jolupbisang.demo.infrastructure.meeting.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FullAudioListQueryService {

    private final MeetingRepository meetingRepository;
    private final FullAudioRepository fullAudioRepository;

    public AudioListRes getCompletedMeetingAudioList(long meetingId, long accessUserId) {

        Meeting meeting = meetingRepository.findByIdWithParticipant(meetingId)
                .orElseThrow(() -> new NotFoundException("meetingId: ", meetingId));

        meeting.validateViewAuthority(accessUserId);

        if (!meeting.isCompleted()) {
            throw new ApplicationException(AudioApplicationErrorCode.MEETING_NOT_COMPLETED);
        }

        List<FullAudio> fullAudios = fullAudioRepository.findAllByMeetingId(meetingId);

        return AudioListRes.from(fullAudios);
    }
}
