package com.jolupbisang.demo.presentation.summary.api;

import com.jolupbisang.demo.application.summary.dto.SummaryListRes;
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
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "요약", description = "회의 요약 관련 API")
public interface SummaryControllerApi {

    @Operation(summary = "요약 조회", description = "특정 회의의 요약 목록을 페이징하여 조회합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "조회 성공", value = """
                                    {
                                        "content": [
                                            {
                                                "id": 1,
                                                "content": "회의 시작 시 논의된 주요 안건에 대한 요약입니다.",
                                                "isRecap": false,
                                                "timestamp": "2023-10-01T10:30:00"
                                            },
                                            {
                                                "id": 2,
                                                "content": "중간 요약: 현재까지 진행된 내용 정리",
                                                "isRecap": true,
                                                "timestamp": "2023-10-01T10:45:00"
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
            @ApiResponse(responseCode = "403", description = "권한없음",
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
    Slice<SummaryListRes> getSummaries(
            @Parameter(description = "회의 ID", required = true, example = "1")
            @PathVariable Long meetingId,
            @Parameter(description = "중간요약 여부 (true: 중간요약만, false: 전체요약만)", example = "false")
            @RequestParam(defaultValue = "false") Boolean isRecap,
            @Parameter(description = "페이징 파라미터 (page: 페이지 번호, size: 페이지 크기, sort: 정렬 조건)")
            @PageableDefault(size = 30, sort = "timestamp") Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails);
}
