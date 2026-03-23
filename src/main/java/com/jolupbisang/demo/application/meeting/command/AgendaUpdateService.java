package com.jolupbisang.demo.application.meeting.command;

import com.jolupbisang.demo.application.meeting.command.dto.AgendaUpdateReq;
import com.jolupbisang.demo.application.meeting.command.dto.AgendaUpdateRes;
import com.jolupbisang.demo.global.exception.NotFoundException;
import com.jolupbisang.demo.domain.meeting.model.Meeting;
import com.jolupbisang.demo.infrastructure.meeting.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AgendaUpdateService {

    private final MeetingRepository meetingRepository;

    @Transactional
    public AgendaUpdateRes updateContent(Long meetingId, Long agendaId, Long accessUserId, AgendaUpdateReq agendaUpdateReq) {

        Meeting meeting = meetingRepository.findByIdWithAllDetail(meetingId)
                .orElseThrow(() -> new NotFoundException("meetingId: %d", meetingId));

        meeting.updateAgenda(agendaId, accessUserId, agendaUpdateReq.content());

        return new AgendaUpdateRes(agendaId);
    }
}
