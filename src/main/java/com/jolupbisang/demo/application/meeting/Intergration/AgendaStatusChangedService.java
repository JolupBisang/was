package com.jolupbisang.demo.application.meeting.Intergration;

import com.jolupbisang.demo.application.meeting.Intergration.dto.AgendaStatusChangedMessage;
import com.jolupbisang.demo.domain.meeting.event.AgendaStatusChangedEvent;
import com.jolupbisang.demo.infrastructure.meeting.MeetingWebsocketManager;
import com.jolupbisang.demo.presentation.audio.dto.response.SocketResponseType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AgendaStatusChangedService {

    private final MeetingWebsocketManager websocketManager;

    @EventListener
    public void sendAgendaStatusChangedMessage(AgendaStatusChangedEvent event) {
        websocketManager.sendToMeeting(
                event.meetingId(),
                SocketResponseType.AGENDA_UPDATED,
                AgendaStatusChangedMessage.of(event.agendaId(), event.isCompleted())
        );
    }
}
