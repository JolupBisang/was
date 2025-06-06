package com.jolupbisang.demo.presentation.segment.api;

import com.jolupbisang.demo.application.segment.dto.SegmentListRes;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "세그먼트", description = "회의 세그먼트 관련 API")
public interface SegmentControllerApi {

    @Operation(summary = "세그먼트 조회", description = "특정 회의의 세그먼트 목록을 페이징하여 조회합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "조회 성공", value = """
                                    {
                                        "content": [
                                            {
                                                "id": 1,
                                                "userId": 123,
                                                "userName": "홍길동",
                                                "segmentOrder": 1,
                                                "timestamp": "2023-10-01T10:30:00",
                                                "text": "안녕하세요, 오늘 회의를 시작하겠습니다.",
                                                "lang": "ko"
                                            },
                                            {
                                                "id": 2,
                                                "userId": 456,
                                                "userName": "김철수",
                                                "segmentOrder": 2,
                                                "timestamp": "2023-10-01T10:30:15",
                                                "text": "네, 안녕하세요.",
                                                "lang": "ko"
                                            }
                                        ],
                                        "pageable": {
                                            "pageNumber": 0,
                                            "pageSize": 30,
                                            "sort": {
                                                "empty": false,
                                                "sorted": true,
                                                "unsorted": false
                                            },
                                            "offset": 0,
                                            "paged": true,
                                            "unpaged": false
                                        },
                                        "size": 30,
                                        "number": 0,
                                        "numberOfElements": 2,
                                        "first": true,
                                        "last": false,
                                        "hasNext": true,
                                        "hasPrevious": false,
                                        "sort": {
                                            "empty": false,
                                            "sorted": true,
                                            "unsorted": false
                                        },
                                        "empty": false
                                    }
                                    """),
                    })),
            @ApiResponse(responseCode = "403", description = "참여자가 아닌 회의",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "조회 실패", value = """
                                        {
                                            "message": "해당 회의의 참여자가 아닙니다.",
                                            "errorId": "570cd8f2-8007-4f4e-bdbb-e9a5dd5d348e",
                                            "errors": null
                                        }
                                    """),
                    }))
    })
    Slice<SegmentListRes> getSegments(
            @Parameter(description = "회의 ID", required = true, example = "1")
            @PathVariable Long meetingId,
            @Parameter(description = "페이징 파라미터 (page: 페이지 번호, size: 페이지 크기, sort: 정렬 조건)")
            @PageableDefault(size = 30, sort = "timestamp") Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails);
} 
