package com.jolupbisang.demo.domain.meeting.model;

import com.jolupbisang.demo.domain.meeting.exception.EmptyAgendaContentException;
import lombok.Getter;

@Getter
public class AgendaDetail {
    private String content;

    public AgendaDetail(String content) {
        setContent(content);
    }

    private void setContent(String content) {
        if (content == null || content.isBlank()) {
            throw new EmptyAgendaContentException();
        }
        this.content = content;
    }
}
