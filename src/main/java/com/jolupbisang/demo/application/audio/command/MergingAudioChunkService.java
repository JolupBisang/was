package com.jolupbisang.demo.application.audio.command;

import com.jolupbisang.demo.application.audio.command.dto.StepFunctionOutput;
import com.jolupbisang.demo.domain.meeting.event.MeetingCompletedEvent;
import com.jolupbisang.demo.infrastructure.aws.sfn.SfnClientUtil;
import com.jolupbisang.demo.infrastructure.meeting.MeetingWebsocketManager;
import com.jolupbisang.demo.presentation.audio.dto.response.SocketResponseType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Service
@RequiredArgsConstructor
public class MergingAudioChunkService {

    private final SfnClientUtil sfnClientUtil;
    private final MeetingWebsocketManager meetingWebsocketManager;

    @Value("${cloud.aws.sfn.merge-audio-state-machine-arn}")
    private String MERGE_AUDIO_STATE_MACHINE_ARN;


    @Order(4)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleMeetingCompletedEvent(MeetingCompletedEvent event) {
        long meetingId = event.meetingId();
        StepFunctionOutput stepFunctionOutput = sfnClientUtil.startMergeAudioStateMachine(MERGE_AUDIO_STATE_MACHINE_ARN, meetingId);

        if (stepFunctionOutput != null) {
            if (stepFunctionOutput.statusCode().equals("400")) {
                log.error("[StepFunction] meetingId:{}, statusCode: {}", event.meetingId(), stepFunctionOutput.statusCode());
            } else {
                log.info("[StepFunction] meetingId:{}, statusCode: {}", event.meetingId(), stepFunctionOutput.statusCode());
            }
        } else {
            log.error("[StepFunction] return null");
        }

        meetingWebsocketManager.sendToMeeting(meetingId, SocketResponseType.MEETING_NOTE_CREATED, "회의록 생성이 완료되었습니다.");
    }
}
