package com.jolupbisang.demo.application.meeting.command;

import com.jolupbisang.demo.application.meeting.command.dto.AgendaCreateReq;
import com.jolupbisang.demo.application.meeting.command.dto.AgendaCreationRes;
import com.jolupbisang.demo.domain.meeting.dto.CreatedAgendaDetail;
import com.jolupbisang.demo.domain.meeting.model.AgendaDetail;
import com.jolupbisang.demo.domain.meeting.model.Meeting;
import com.jolupbisang.demo.global.exception.NotFoundException;
import com.jolupbisang.demo.infrastructure.meeting.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgendaCreationService {

    private final MeetingRepository meetingRepository;

    @Transactional
    public AgendaCreationRes create(long meetingId, long accessUserId, AgendaCreateReq agendaCreateReq) {

        Meeting meeting = meetingRepository.findByIdWithAllDetail(meetingId)
                .orElseThrow(() -> new NotFoundException("meetingId: %d", meetingId));

        List<AgendaDetail> agendaDetails = agendaCreateReq.contents().stream()
                .map(AgendaDetail::new)
                .toList();

        meeting.addAgendas(agendaDetails, accessUserId);

        meetingRepository.save(meeting);

        List<CreatedAgendaDetail> agendas = meeting.getAgendas().stream()
                .map(agenda -> new CreatedAgendaDetail(agenda.getId(), agenda.getContent())).toList();


        return AgendaCreationRes.of(meetingId, agendas);
    }
} 
