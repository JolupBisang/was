package com.jolupbisang.demo.application.summary.command;

import com.jolupbisang.demo.application.summary.event.SummaryReceivedEvent;
import com.jolupbisang.demo.domain.summary.Summary;
import com.jolupbisang.demo.infrastructure.summary.SummaryRepository;
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
public class SummaryReceivedEventListener {

    private final SummaryRepository summaryRepository;
    private final UserRepository userRepository;

    @Transactional
    @EventListener
    public void handleSummaryReceived(SummaryReceivedEvent event) {
        String content = event.content();
        if (!event.ids().isEmpty()) {
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

            content = String.format(event.content(), (Object[]) nicknames);
        }

        summaryRepository.save(
                new Summary(
                        event.meetingId(),
                        content,
                        event.isRecap(),
                        event.generatedDateTime()
                )
        );
    }
}
