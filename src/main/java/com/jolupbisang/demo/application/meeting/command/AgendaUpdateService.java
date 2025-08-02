package com.jolupbisang.demo.application.meeting.command;

import com.jolupbisang.demo.application.meeting.command.dto.AgendaUpdateReq;
import com.jolupbisang.demo.application.meeting.command.dto.AgendaUpdateRes;
import com.jolupbisang.demo.application.meeting.exception.MeetingNotFoundException;
import com.jolupbisang.demo.domain.meeting.model.Meeting;
import com.jolupbisang.demo.infrastructure.meeting.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AgendaUpdateService {

    private final MeetingRepository meetingRepository;

    public AgendaUpdateRes updateContent(Long meetingId, Long agendaId, Long accessUserId, AgendaUpdateReq agendaUpdateReq) {

        Meeting meeting = meetingRepository.findByIdWithAllDetail(meetingId)
                .orElseThrow(() -> new MeetingNotFoundException(Map.of("meetingId", meetingId)));

        meeting.updateAgenda(agendaId, accessUserId, agendaUpdateReq.content());

        return new AgendaUpdateRes(agendaId);
    }
}
