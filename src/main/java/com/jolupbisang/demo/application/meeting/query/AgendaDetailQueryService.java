package com.jolupbisang.demo.application.meeting.query;

import com.jolupbisang.demo.application.meeting.query.dto.AgendaListRes;
import com.jolupbisang.demo.domain.meeting.model.Agenda;
import com.jolupbisang.demo.domain.meeting.model.Meeting;
import com.jolupbisang.demo.global.exception.NotFoundException;
import com.jolupbisang.demo.infrastructure.meeting.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgendaDetailQueryService {
    private final MeetingRepository meetingRepository;

    @Transactional(readOnly = true)
    public AgendaListRes getAgendas(long meetingId, long userId) {

        Meeting meeting = meetingRepository.findByIdWithAllDetail(meetingId)
                .orElseThrow(() -> new NotFoundException("meetingId: %d", meetingId));

        meeting.validateViewAuthority(userId);

        List<Agenda> agendas = meeting.getAgendas();

        return AgendaListRes.from(agendas);
    }
}
