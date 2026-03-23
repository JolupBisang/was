package com.jolupbisang.demo.domain.meeting.model;

import com.jolupbisang.demo.domain.meeting.exception.MeetingDomainErrorCode;
import com.jolupbisang.demo.global.exception.DomainException;
import lombok.Getter;

@Getter
public class AgendaDetail {
    private String content;

    public AgendaDetail(String content) {
        setContent(content);
    }

    private void setContent(String content) {
        if (content == null || content.isBlank()) {
            throw new DomainException(MeetingDomainErrorCode.EMPTY_AGENDA_CONTENT);
        }
        this.content = content;
    }
}
