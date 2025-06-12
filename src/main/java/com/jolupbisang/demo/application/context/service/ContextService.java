package com.jolupbisang.demo.application.context.service;

import com.jolupbisang.demo.application.event.*;
import com.jolupbisang.demo.application.event.whisper.WhisperContextEvent;
import com.jolupbisang.demo.infrastructure.audio.client.WhisperClient;
import com.jolupbisang.demo.infrastructure.audio.client.dto.response.ContextResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContextService {

    private final WhisperClient whisperClient;
    private final TaskScheduler taskScheduler;
    private final ApplicationEventPublisher eventPublisher;
    private final Map<Long, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    private static final int CONTEXT_SEND_INTERVAL_MINUTES = 5;

    @Async("AsyncTaskExecutor")
    @EventListener
    public void handleMeetingStart(MeetingStartingEvent event) {
        long meetingId = event.getMeetingId();
        Runnable task = () -> whisperClient.sendContext(meetingId);

        ScheduledFuture<?> scheduledFuture = taskScheduler.scheduleAtFixedRate(
                task,
                Instant.now().plusSeconds(60 * CONTEXT_SEND_INTERVAL_MINUTES),
                Duration.ofMinutes(CONTEXT_SEND_INTERVAL_MINUTES));
        scheduledTasks.put(meetingId, scheduledFuture);
    }

    @Order(1)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleMeetingCompletion(MeetingCompletedEvent event) {
        Long meetingId = event.getMeetingId();
        ScheduledFuture<?> scheduledFuture = scheduledTasks.get(meetingId);

        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
            scheduledTasks.remove(meetingId);
            log.info("Cancelled periodic context sending for completed meeting: {}", meetingId);
        }
    }

    @EventListener
    public void handleContextReceived(WhisperContextEvent event) {
        ContextResponse contextResponse = event.getContextResponse();
        Object source = event.getSource();
        long meetingId = contextResponse.groupId();
        boolean isRecap = contextResponse.isRecap();

        String context = contextResponse.context();
        if (context != null && !context.isEmpty()) {
            eventPublisher.publishEvent(new SummaryReceivedEvent(source, meetingId, context, isRecap));
        }

        List<Integer> agenda = contextResponse.agenda();
        if (agenda != null && !agenda.isEmpty()) {
            eventPublisher.publishEvent(new AgendaReceivedEvent(source, meetingId, agenda));
        }

        List<ContextResponse.FeedbackRes> feedbackResList = contextResponse.feedback();
        if (feedbackResList != null && !feedbackResList.isEmpty()) {
            for (ContextResponse.FeedbackRes feedbackRes : feedbackResList) {
                if (feedbackRes != null && feedbackRes.userId() != null && feedbackRes.comment() != null && !feedbackRes.comment().isEmpty()) {
                    eventPublisher.publishEvent(new FeedbackReceivedEvent(source, meetingId, feedbackRes.userId(), feedbackRes.comment()));
                }
            }
        }
    }


    /*
     * 테스트용 스케줄러입니다. 주석을 풀고 싶으시면 시작후 1분마다 피드백과 중간요약을 제공합니다.
     * userId 부분을 꼭 자기의 userId로 바꾸고 실행해야합니다!!
     */
//    @Async("AsyncTaskExecutor")
//    @EventListener
//    public void makeTestTask(MeetingStartingEvent event) {
//        long meetingId = event.getMeetingId();
//        long userId = 1L;
//        final int[] orderCounter = {0};
//        final int[] currentWordStartTime = {16000};
//
//        Runnable task1 = () -> {
//            log.info("This is test - publishing DiarizedEvent with incrementing order");
//            eventPublisher.publishEvent(new SummaryReceivedEvent(this, meetingId, "Test summary", false));
//            eventPublisher.publishEvent(new FeedbackReceivedEvent(this, meetingId, userId, "Test feedback"));
//        };
//
//        Runnable task2 = () -> {
//            int currentWordStart = currentWordStartTime[0];
//            DiarizedResponse.Word testWord = new DiarizedResponse.Word(
//                    currentWordStart,
//                    currentWordStart + 1000,
//                    "test_word",
//                    "ko"
//            );
//            currentWordStartTime[0] = currentWordStart + 16000; // Increment global counter
//
//            DiarizedResponse.Segment completedSegment = new DiarizedResponse.Segment(
//                    orderCounter[0],
//                    List.of("ko"),
//                    "그쵸 아마 이 질문의 의도는 제 생각하기에 높은 산곡대기로 가면 어쨌든 그 고도만큼 올라가니까 그렇긴 하겠네요.",
//                    List.of(testWord),
//                    1L,
//                    1L
//            );
//            List<DiarizedResponse.Segment> completedList = new ArrayList<>();
//            completedList.add(completedSegment);
//
//            DiarizedResponse.Segment candidateSegment1 = new DiarizedResponse.Segment(
//                    orderCounter[0]++,
//                    List.of("ko"),
//                    "지표면에서에 비해서는 중력을 더 약하게 느끼지 않겠냐 그러면 지구 중력을 벗어나려고 결국에 연료를.",
//                    List.of(testWord),
//                    1L,
//                    1L
//            );
//            List<DiarizedResponse.Segment> candidateList = new ArrayList<>();
//            candidateList.add(candidateSegment1);
//
//            DiarizedResponse diarizedResponseForTest = new DiarizedResponse(WhisperResponseType.DIARIZED, meetingId, completedList, candidateList);
//
//            eventPublisher.publishEvent(new WhisperDiarizedEvent(diarizedResponseForTest));
//        };
//
//        taskScheduler.scheduleAtFixedRate(task1, Instant.now().plusSeconds(10), Duration.ofMinutes(1));
//        taskScheduler.scheduleAtFixedRate(task2, Instant.now().plusSeconds(1), Duration.ofSeconds(1));
//    }

} 
