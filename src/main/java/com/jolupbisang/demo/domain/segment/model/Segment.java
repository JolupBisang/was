package com.jolupbisang.demo.domain.segment.model;

import com.jolupbisang.demo.domain.common.BaseTimeEntity;
import com.jolupbisang.demo.domain.segment.exception.*;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Segment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "meeting_id", nullable = false)
    private Long meetingId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "audio_user_id", nullable = false)
    private Long audioUserId;

    @Column(name = "segment_order", nullable = false)
    private Integer order;

    @ElementCollection
    @CollectionTable(
            name = "segment_word",
            joinColumns = @JoinColumn(name = "segment_id")
    )
    private List<Word> words;


    @Column(nullable = false, length = 500)
    private String text;

    @Column(length = 10, nullable = false)
    private String lang;

    @Column(name = "spoken_date_time")
    private LocalDateTime spokenDateTime;

    private static final String DEFAULT_LANG = "ko";

    public Segment(long meetingId, long userId, long audioUserId, int order, List<Word> words, String text, String lang, LocalDateTime spokenDateTime) {
        setMeetingId(meetingId);
        setUserId(userId);
        setAudioUserId(audioUserId);
        setOrder(order);
        setWords(words);
        setText(text);
        setLang(lang);
        setSpokenDateTime(spokenDateTime);
    }

    public void update(long userId, long audioUserId, List<Word> words, String text, String lang, LocalDateTime spokenDateTime) {
        setUserId(userId);
        setAudioUserId(audioUserId);
        setWords(words);
        setText(text);
        setLang(lang);
        setSpokenDateTime(spokenDateTime);
    }

    private void setMeetingId(long meetingId) {
        if (meetingId < 0) {
            throw new InvalidMeetingIdException(Map.of("meetingId", meetingId));
        }
        this.meetingId = meetingId;
    }

    private void setUserId(long userId) {
        if (userId < 0) {
            throw new InvalidUserIdException(Map.of("userId", userId));
        }
        this.userId = userId;
    }

    private void setAudioUserId(long audioUserId) {
        if (audioUserId < 0) {
            throw new InvalidUserIdException(Map.of("audioUserId", audioUserId));
        }
        this.audioUserId = audioUserId;
    }

    private void setOrder(Integer order) {
        if (order == null) {
            throw new InvalidOrderException(Map.of("order", order));
        }
        this.order = order;
    }

    private void setWords(List<Word> words) {
        this.words = words;
    }

    private void setText(String text) {
        if (text == null || text.isEmpty()) {
            throw new EmptySegmentTextException(Map.of("text", text));
        }
        this.text = text;
    }

    private void setLang(String lang) {
        if (lang == null || lang.isEmpty()) {
            this.lang = DEFAULT_LANG;
        } else {
            this.lang = lang;
        }
    }

    private void setSpokenDateTime(LocalDateTime spokenDateTime) {
        if (spokenDateTime == null) {
            throw new NullSpokenDateTimeException(Map.of("spokenDateTime", spokenDateTime));
        }
        this.spokenDateTime = spokenDateTime;
    }
}
