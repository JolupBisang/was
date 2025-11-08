package com.jolupbisang.demo.application.meeting.Intergration;

import com.jolupbisang.demo.application.meeting.event.MeetingSessionClosedEvent;
import com.jolupbisang.demo.domain.meeting.event.MeetingCompletedEvent;
import com.jolupbisang.demo.domain.meeting.model.MeetingCompletedOrder;
import com.jolupbisang.demo.infrastructure.meeting.MeetingWebsocketManager;
import com.jolupbisang.demo.presentation.audio.dto.response.SocketResponseType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeetingWebsocketRemovalService {

    private final MeetingWebsocketManager websocketManager;

    @EventListener
    public void handleMeetingSessionClosedEvent(MeetingSessionClosedEvent event) {
        websocketManager.removeUserOfMeeting(
                event.meetingId(),
                event.userId()
        );
    }

    @Order(MeetingCompletedOrder.COMPLETED_EVENT_SENDING)
    @EventListener
    public void sendCompletedEvent(MeetingCompletedEvent event) {
        websocketManager.sendToMeeting(event.meetingId(), SocketResponseType.COMPLETION_SCHEDULED, "회의가 종료되었습니다. 추가작업을 진행합니다.");
    }

    @Order(MeetingCompletedOrder.CLEAN_UP_SESSION)
    @EventListener
    public void cleanUpSession(MeetingCompletedEvent event) {
        websocketManager.sendToMeeting(event.meetingId(), SocketResponseType.MEETING_COMPLETED, "회의가 종료되었습니다.");
        websocketManager.closeAllSessionOfMeeting(event.meetingId());
    }
}
