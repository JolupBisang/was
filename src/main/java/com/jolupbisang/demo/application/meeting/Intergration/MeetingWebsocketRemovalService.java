package com.jolupbisang.demo.application.meeting.Intergration;

import com.jolupbisang.demo.application.meeting.event.MeetingSessionClosedEvent;
import com.jolupbisang.demo.infrastructure.meeting.MeetingWebsocketManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
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
}
