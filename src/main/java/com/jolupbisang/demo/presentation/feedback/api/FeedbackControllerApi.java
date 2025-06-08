package com.jolupbisang.demo.presentation.feedback.api;

import com.jolupbisang.demo.application.feedback.dto.FeedbackListRes;
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

@Tag(name = "피드백", description = "회의 실시간 피드백 관련 API")
public interface FeedbackControllerApi {

    @Operation(summary = "피드백 조회", description = "특정 회의의 피드백 목록을 페이징하여 조회합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "조회 성공", value = """
                                    {
                                        "content": [
                                            {
                                                "id": 1,
                                                "comment": "회의 진행이 너무 빨라요. 조금 천천히 해주세요.",
                                                "timestamp": "2023-10-01T10:30:00"
                                            },
                                            {
                                                "id": 2,
                                                "comment": "목소리가 잘 안들려요.",
                                                "timestamp": "2023-10-01T10:32:15"
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
                    })),
    })
    Slice<FeedbackListRes> getFeedbacks(
            @Parameter(description = "회의 ID", required = true, example = "1")
            @PathVariable Long meetingId,
            @Parameter(description = "페이징 파라미터 (page: 페이지 번호, size: 페이지 크기(최대 40), sort: 정렬 조건)")
            @PageableDefault(size = 30, sort = "timestamp") Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails);
}
