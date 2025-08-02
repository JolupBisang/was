package com.jolupbisang.demo.application.meeting.command;

import com.jolupbisang.demo.application.meeting.command.dto.AgendaDeletionRes;
import com.jolupbisang.demo.application.meeting.exception.MeetingNotFoundException;
import com.jolupbisang.demo.domain.meeting.model.Meeting;
import com.jolupbisang.demo.infrastructure.meeting.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AgendaDeletionService {

    private final MeetingRepository meetingRepository;

    public AgendaDeletionRes delete(long meetingId, long agendaId, long userId) {
        Meeting meeting = meetingRepository.findByIdWithAllDetail(meetingId)
                .orElseThrow(() -> new MeetingNotFoundException(Map.of("meetingId", meetingId)));

        meeting.deleteAgenda(agendaId, userId);

        return new AgendaDeletionRes(meetingId, agendaId);
    }
} 
