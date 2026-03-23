package com.jolupbisang.demo.domain.segment.model;

import com.jolupbisang.demo.domain.common.BaseTimeEntity;
import com.jolupbisang.demo.domain.segment.exception.SegmentDomainErrorCode;
import com.jolupbisang.demo.global.exception.DomainException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    @Column(nullable = false, length = 500)
    private String text;

    @Column(length = 10, nullable = false)
    private String lang;

    @Column(name = "spoken_date_time")
    private LocalDateTime spokenDateTime;

    @Column
    private LocalDateTime translatedDateTime;

    private static final String DEFAULT_LANG = "ko";

    public Segment(long meetingId, long userId, long audioUserId, int order, String text, String lang, LocalDateTime spokenDateTime, LocalDateTime translatedDateTime) {
        setMeetingId(meetingId);
        setUserId(userId);
        setAudioUserId(audioUserId);
        setOrder(order);
        setText(text);
        setLang(lang);
        setSpokenDateTime(spokenDateTime);
        setTranslatedDateTime(translatedDateTime);
    }

    public void update(long userId, long audioUserId, String text, String lang, LocalDateTime spokenDateTime, LocalDateTime translatedDateTime) {
        setUserId(userId);
        setAudioUserId(audioUserId);
        setText(text);
        setLang(lang);
        setSpokenDateTime(spokenDateTime);
        setTranslatedDateTime(translatedDateTime);
    }

    private void setMeetingId(long meetingId) {
        if (meetingId < 0) {
            throw new DomainException(SegmentDomainErrorCode.INVALID_MEETING_ID, "meetingId: %d", meetingId);
        }
        this.meetingId = meetingId;
    }

    private void setUserId(long userId) {
        if (userId < 0) {
            throw new DomainException(SegmentDomainErrorCode.INVALID_USER_ID, "userId: %d", userId);
        }
        this.userId = userId;
    }

    private void setAudioUserId(long audioUserId) {
        if (audioUserId < 0) {
            throw new DomainException(SegmentDomainErrorCode.INVALID_USER_ID, "audioUserId: %d", audioUserId);
        }
        this.audioUserId = audioUserId;
    }

    private void setOrder(int order) {
        this.order = order;
    }

    private void setText(String text) {
        if (text == null || text.isEmpty()) {
            throw new DomainException(SegmentDomainErrorCode.EMPTY_SEGMENT_TEXT);
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
            throw new DomainException(SegmentDomainErrorCode.NULL_SPOKEN_DATE_TIME);
        }
        this.spokenDateTime = spokenDateTime;
    }

    private void setTranslatedDateTime(LocalDateTime translatedDateTime) {
        if (translatedDateTime == null) {
            throw new DomainException(SegmentDomainErrorCode.NULL_SPOKEN_DATE_TIME);
        }
        this.translatedDateTime = translatedDateTime;
    }
}
