package com.jolupbisang.demo.application.feedback.command;

import com.jolupbisang.demo.application.feedback.event.FeedbackReceivedEvent;
import com.jolupbisang.demo.domain.feedback.Feedback;
import com.jolupbisang.demo.infrastructure.feedback.FeedbackRepository;
import com.jolupbisang.demo.infrastructure.user.UserRepository;
import com.jolupbisang.demo.infrastructure.user.dto.UserSummary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class FeedbackReceivedEventListener {

    private final UserRepository userRepository;
    private final FeedbackRepository feedbackRepository;

    @EventListener
    @Transactional
    public void handleFeedbackEvent(FeedbackReceivedEvent event) {
        Map<Long, String> idToNicknameMap = userRepository.findIdAndNicknameByIdIn(event.ids()).stream()
                .collect(Collectors.toMap(UserSummary::id, UserSummary::nickname));

        String[] nicknames = new String[event.ids().size()];
        for (int i = 0; i < event.ids().size(); i++) {
            Long userId = event.ids().get(i);
            String nickname = idToNicknameMap.get(userId);
            if (nickname == null) {
                log.error("User not found for id: {}. Total ids: {}, Found nicknames: {}",
                        userId, event.ids().size(), idToNicknameMap.size());
                return;
            }
            nicknames[i] = nickname;
        }

        // nickname들을 String.format에 적용
        String comment = String.format(event.comment(), (Object[]) nicknames);

        feedbackRepository.save(
                new Feedback(
                        event.userId(),
                        event.meetingId(),
                        comment,
                        event.generatedDateTime()
                ));
    }
}
