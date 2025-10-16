package com.jolupbisang.demo.application.meeting.command;

import com.jolupbisang.demo.application.meeting.command.dto.AgendaStatusChangeRes;
import com.jolupbisang.demo.global.exception.NotFoundException;
import com.jolupbisang.demo.domain.meeting.model.Meeting;
import com.jolupbisang.demo.infrastructure.meeting.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AgendaStatusChangeService {

    private final MeetingRepository meetingRepository;

    @Transactional
    public AgendaStatusChangeRes changeAgendaStatus(long meetingId, long agendaId, long accessUserId, boolean isCompleted) {
        Meeting meeting = meetingRepository.findByIdWithAgenda(meetingId, agendaId)
                .orElseThrow(() -> new NotFoundException("meetingId: %d", meetingId));

        meeting.changeAgendaStatus(agendaId, accessUserId, isCompleted);

        return new AgendaStatusChangeRes(meetingId, agendaId, isCompleted);
    }
}
