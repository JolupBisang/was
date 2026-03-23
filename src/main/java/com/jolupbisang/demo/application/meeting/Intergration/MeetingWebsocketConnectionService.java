package com.jolupbisang.demo.application.meeting.Intergration;

import com.jolupbisang.demo.application.meeting.Intergration.dto.MeetingSessionConnectedMessage;
import com.jolupbisang.demo.application.meeting.exception.MeetingApplicationErrorCode;
import com.jolupbisang.demo.domain.meeting.model.Meeting;
import com.jolupbisang.demo.global.exception.ApplicationException;
import com.jolupbisang.demo.global.exception.NotFoundException;
import com.jolupbisang.demo.infrastructure.audio.AudioProgressRepository;
import com.jolupbisang.demo.infrastructure.meeting.MeetingRepository;
import com.jolupbisang.demo.infrastructure.meeting.MeetingWebsocketManager;
import com.jolupbisang.demo.presentation.audio.dto.response.SocketResponseType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class MeetingWebsocketConnectionService {

    private final MeetingRepository meetingRepository;
    private final AudioProgressRepository audioProgressRepository;
    private final MeetingWebsocketManager websocketManager;

    public void registerSessionToMeeting(WebSocketSession session, long meetingId, long userId) {
        Meeting meeting = meetingRepository.findByIdWithParticipant(meetingId)
                .orElseThrow(() -> new NotFoundException("meetingId: %d", meetingId));

        if (!meeting.isParticipant(userId)) {
            throw new ApplicationException(MeetingApplicationErrorCode.NOT_PARTICIPANT, "userId: %d", userId);
        }

        if (!meeting.isInProgress()) {
            throw new ApplicationException(MeetingApplicationErrorCode.NOT_IN_PROGRESS_MEETING, "meetingId: %d", meetingId);
        }

        websocketManager.addToMeeting(meetingId, userId, session);
        sendEstablishedMessage(session, meetingId, userId, meeting);
    }

    private void sendEstablishedMessage(WebSocketSession session, long meetingId, long userId, Meeting meeting) {
        long lastProcessedChunkId = audioProgressRepository.findLastProcessedChunkId(meetingId, userId)
                .orElse(-1L);

        LocalDateTime actualStartTime = meeting.getActualProgressTime().getActualStartTime();

        websocketManager.sendToUserOfMeeting(
                meetingId,
                userId,
                SocketResponseType.CONNECTION_ESTABLISHED,
                MeetingSessionConnectedMessage.of(actualStartTime, lastProcessedChunkId));
    }
}
