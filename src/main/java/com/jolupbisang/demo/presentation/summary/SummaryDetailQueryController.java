package com.jolupbisang.demo.presentation.summary;

import com.jolupbisang.demo.application.summary.query.SummaryDetailQueryService;
import com.jolupbisang.demo.application.summary.query.dto.SummaryListRes;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/summary")
public class SummaryDetailQueryController {

    private final SummaryDetailQueryService summaryDetailQueryService;

    @GetMapping("/{meetingId}")
    public Slice<SummaryListRes> getSummaries(@PathVariable long meetingId,
                                              @RequestParam(defaultValue = "false") boolean isRecap,
                                              @PageableDefault(size = 30, sort = "timestamp", direction = Sort.Direction.DESC) Pageable pageable,
                                              @AuthenticationPrincipal CustomUserDetails userDetails) {

        return summaryDetailQueryService.getSummaries(meetingId, userDetails.getUserId(), isRecap, pageable);
    }
}
