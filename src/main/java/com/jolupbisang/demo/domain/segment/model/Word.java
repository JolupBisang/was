package com.jolupbisang.demo.domain.segment.model;

import com.jolupbisang.demo.domain.segment.exception.SegmentDomainErrorCode;
import com.jolupbisang.demo.global.exception.DomainException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class Word {

    @Column(name = "start_chunk")
    Integer startChunk;

    @Column(name = "end_chunk")
    Integer endChunk;

    @Column(length = 1000)
    String text;

    String lang;

    private static final String DEFAULT_LANG = "ko";

    public Word(Integer startChunk, Integer endChunk, String text, String lang) {
        setStartChunk(startChunk);
        setEndChunk(endChunk);
        setText(text);
        setLang(lang);
        validateChunkOrder(startChunk, endChunk);
    }

    private void setStartChunk(Integer startChunk) {
        if (startChunk == null) {
            throw new DomainException(SegmentDomainErrorCode.NULL_START_CHUNK);
        }
        this.startChunk = startChunk;
    }

    private void setEndChunk(Integer endChunk) {
        if (endChunk == null) {
            throw new DomainException(SegmentDomainErrorCode.NULL_END_CHUNK);
        }
        this.endChunk = endChunk;
    }

    private void setText(String text) {
        if (text == null || text.isEmpty()) {
            throw new DomainException(SegmentDomainErrorCode.EMPTY_TEXT);
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

    private void validateChunkOrder(Integer startChunk, Integer endChunk) {
        if (startChunk > endChunk) {
            throw new DomainException(SegmentDomainErrorCode.INVALID_SEGMENT_CHUNK_ORDER, "startChunk: %d, endChunk: %d", startChunk, endChunk);
        }
    }
}
