package com.jolupbisang.demo.domain.segment.model;

import com.jolupbisang.demo.domain.segment.exception.EmptyTextException;
import com.jolupbisang.demo.domain.segment.exception.InvalidSegmentChunkOrderException;
import com.jolupbisang.demo.domain.segment.exception.NullEndChunkException;
import com.jolupbisang.demo.domain.segment.exception.NullStartChunkException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

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
            throw new NullStartChunkException(Map.of("startChunk", startChunk));
        }
        this.startChunk = startChunk;
    }

    private void setEndChunk(Integer endChunk) {
        if (endChunk == null) {
            throw new NullEndChunkException(Map.of("endChunk", endChunk));
        }
        this.endChunk = endChunk;
    }

    private void setText(String text) {
        if (text == null || text.isEmpty()) {
            throw new EmptyTextException(Map.of("text", text));
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
            throw new InvalidSegmentChunkOrderException(Map.of("startChunk", startChunk, "endChunk", endChunk));
        }
    }
}
