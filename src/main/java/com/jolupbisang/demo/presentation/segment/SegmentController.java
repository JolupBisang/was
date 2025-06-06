package com.jolupbisang.demo.presentation.segment;

import com.jolupbisang.demo.application.segment.SegmentService;
import com.jolupbisang.demo.application.segment.dto.SegmentListRes;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import com.jolupbisang.demo.presentation.segment.api.SegmentControllerApi;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/segment")
public class SegmentController implements SegmentControllerApi {

    private final SegmentService segmentService;

    @GetMapping("/{meetingId}")
    public Slice<SegmentListRes> getSegments(@PathVariable Long meetingId,
                                             @PageableDefault(size = 40, sort = "segmentOrder", direction = Sort.Direction.DESC) Pageable pageable,
                                             @AuthenticationPrincipal CustomUserDetails userDetails) {

        return segmentService.getSegments(meetingId, userDetails.getUserId(), pageable);
    }
} 
