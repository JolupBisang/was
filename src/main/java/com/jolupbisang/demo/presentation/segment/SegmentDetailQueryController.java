package com.jolupbisang.demo.presentation.segment;

import com.jolupbisang.demo.application.segment.query.SegmentDetailQueryService;
import com.jolupbisang.demo.application.segment.query.dto.SegmentDetailRes;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SegmentDetailQueryController {

    private final SegmentDetailQueryService segmentDetailQueryService;

    @GetMapping("/api/v1/meetings/{meetingId}/segments")
    public Slice<SegmentDetailRes> getSegments(@PathVariable Long meetingId,
                                               @PageableDefault(size = 40, sort = "order", direction = Sort.Direction.DESC) Pageable pageable,
                                               @AuthenticationPrincipal CustomUserDetails userDetails) {

        return segmentDetailQueryService.getSegmentDetails(meetingId, userDetails.getUserId(), pageable);
    }
}
