package com.jolupbisang.demo.presentation.agenda.api;

import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import com.jolupbisang.demo.presentation.agenda.dto.request.AgendaCreateReq;
import com.jolupbisang.demo.presentation.agenda.dto.request.AgendaStatusReq;
import com.jolupbisang.demo.presentation.agenda.dto.request.AgendaUpdateReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "회의 안건", description = "회의 안건 관련 API")
public interface AgendaControllerApi {

    @Operation(summary = "회의 안건 상태 변경", description = "회의 안건의 완료 상태를 변경합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "안건 상태 변경 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "변경 성공", value = """
                                        {
                                            "message": "회의 안건 상태변경 성공",
                                            "data": {
                                                "isCompleted": true
                                            }
                                        }
                                    """),
                    })),
            @ApiResponse(responseCode = "422", description = "DTO 검증 실패",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "변경 실패", value = """
                                        {
                                            "message": "잘못된 입력입니다.",
                                            "errorId": "dc0bf0b6-484c-4151-9597-eb10d57cba91",
                                            "errors": {
                                                "isCompleted": "완료 상태는 null일 수 없습니다."
                                            }
                                        }
                                    """),
                    })),
            @ApiResponse(responseCode = "403", description = "권한 없음 또는 없는 회의",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "변경 실패", value = """
                                        {
                                            "message": "해당 회의의 참여자가 아닙니다.",
                                            "errorId": "5d458520-6cd7-404f-ab91-234bd05fbb66",
                                            "errors": null
                                        }
                                    """),
                    })),
    })
    ResponseEntity<?> changeAgendaStatus(AgendaStatusReq agendaStatusReq,
                                         @PathVariable("agendaId") Long agendaId,
                                         @AuthenticationPrincipal CustomUserDetails customUserDetails);

    @Operation(summary = "회의 안건 목록 조회", description = "특정 회의의 안건 목록을 조회합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "안건 목록 조회 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "조회 성공", value = """
                                        {
                                            "message": "회의 안건 조회 성공",
                                            "data": {
                                                "agendaDetails": [
                                                    {
                                                        "agendaId": 1,
                                                        "content": "회의 안건1",
                                                        "isCompleted": true
                                                    },
                                                    {
                                                        "agendaId": 2,
                                                        "content": "회의 안건2",
                                                        "isCompleted": false
                                                    }
                                                ]
                                            }
                                        }
                                    """),
                    })),
            @ApiResponse(responseCode = "403", description = "권한 없음 또는 없는 회의",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "조회 실패", value = """
                                        {
                                            "message": "해당 회의의 참여자가 아닙니다.",
                                            "errorId": "4109fecb-a380-45a8-a200-0bc2ce01ce76",
                                            "errors": null
                                        }
                                    """),
                    })),
    })
    ResponseEntity<?> getAgendas(@PathVariable("meetingId") Long meetingId,
                                 @AuthenticationPrincipal CustomUserDetails customUserDetails);

    @Operation(summary = "회의 안건 추가", description = "회의에 새로운 안건을 추가합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "안건 추가 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "추가 성공", value = """
                                        {
                                            "message": "회의 안건 추가 성공",
                                            "data": {
                                                "agendaId": 77
                                            }
                                        }
                                    """),
                    })),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (완료되었거나 취소된 회의)",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "추가 실패 - 완료되었거나 취소된 회의", value = """
                                        {
                                            "message": "완료되었거나 취소된 회의에는 안건을 추가할 수 없습니다.",
                                            "errorId": "ff9cc760-3323-440b-ad3c-41d9fc01d2da",
                                            "errors": null
                                        }
                                    """),
                    })),
            @ApiResponse(responseCode = "403", description = "권한 없음 (호스트가 아님)",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "추가 실패 - 권한 없음", value = """
                                        {
                                            "message": "해당 작업은 회의 리더만 수행할 수 있습니다.",
                                            "errorId": "53f960f2-fa6f-4247-bac6-87b469fb5118",
                                            "errors": null
                                    """),
                    })),
            @ApiResponse(responseCode = "422", description = "DTO 검증 실패",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "추가 실패 - DTO 검증 실패", value = """
                                        {
                                            "message": "잘못된 입력입니다.",
                                            "errorId": "c8c38050-d2d7-431f-93f9-4280413d9006",
                                            "errors": {
                                                "content": "안건 내용은 필수입니다."
                                            }
                                        }
                                    """),
                    }))
    })
    ResponseEntity<?> addAgenda(@PathVariable("meetingId") Long meetingId,
                                @RequestBody @Valid AgendaCreateReq agendaCreateReq,
                                @AuthenticationPrincipal CustomUserDetails customUserDetails);

    @Operation(summary = "회의 안건 수정", description = "회의 안건의 내용을 수정합니다 (대기 중인 회의에서만 가능)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "안건 수정 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "수정 성공", value = """
                                        {
                                            "message": "회의 안건 수정 성공",
                                            "data": null
                                        }
                                    """),
                    })),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (대기 중인 회의가 아님)",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "수정 실패 - 대기 중인 회의가 아님", value = """
                                        {
                                            "message": "대기 중인 회의에서만 안건을 수정할 수 있습니다.",
                                            "errorId": "57c477a2-8201-408d-b4ac-3c664daa08f0",
                                            "errors": null
                                        }
                                    """),
                    })),
            @ApiResponse(responseCode = "403", description = "권한 없음 (호스트가 아님)",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "수정 실패 - 권한 없음", value = """
                                        {
                                            "message": "해당 작업은 회의 리더만 수행할 수 있습니다.",
                                            "errorId": "e6a96ddf-4a50-40fe-9c52-07649a1cca9f",
                                            "errors": null
                                        }
                                    """),
                    })),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 안건",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "수정 실패 - 존재하지 않는 안건", value = """
                                        {
                                            "message": "존재하지 않는 안건입니다.",
                                            "errorId": "816bc13a-a4e8-420c-a70e-2274f2f8fba3",
                                            "errors": null
                                        }
                                    """),
                    })),
            @ApiResponse(responseCode = "422", description = "DTO 검증 실패",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "수정 실패 - DTO 검증 실패", value = """
                                        {
                                            "message": "잘못된 입력입니다.",
                                            "errorId": "c8c38050-d2d7-431f-93f9-4280413d9006",
                                            "errors": {
                                                "content": "안건 내용은 필수입니다."
                                            }
                                        }
                                    """),
                    }))
    })
    ResponseEntity<?> updateAgenda(@PathVariable("agendaId") Long agendaId,
                                   @RequestBody @Valid AgendaUpdateReq agendaUpdateReq,
                                   @AuthenticationPrincipal CustomUserDetails customUserDetails);

    @Operation(summary = "회의 안건 삭제", description = "회의 안건을 삭제합니다 (대기 중인 회의에서만 가능)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "안건 삭제 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "삭제 성공", value = """
                                        {
                                            "message": "회의 안건 삭제 성공",
                                            "data": null
                                        }
                                    """),
                    })),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (대기 중인 회의가 아님)",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "삭제 실패 - 대기 중인 회의가 아님", value = """
                                        {
                                            "message": "대기 중인 회의에서만 안건을 삭제할 수 있습니다.",
                                            "errorId": "57c477a2-8201-408d-b4ac-3c664daa08f0",
                                            "errors": null
                                        }
                                    """),
                    })),
            @ApiResponse(responseCode = "403", description = "권한 없음 (호스트가 아님)",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "삭제 실패 - 권한 없음", value = """
                                        {
                                            "message": "해당 작업은 회의 리더만 수행할 수 있습니다.",
                                            "errorId": "e6a96ddf-4a50-40fe-9c52-07649a1cca9f",
                                            "errors": null
                                        }
                                    """),
                    })),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 안건",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "삭제 실패 - 존재하지 않는 안건", value = """
                                        {
                                            "message": "존재하지 않는 안건입니다.",
                                            "errorId": "816bc13a-a4e8-420c-a70e-2274f2f8fba3",
                                            "errors": null
                                        }
                                    """),
                    }))
    })
    ResponseEntity<?> deleteAgenda(@PathVariable("agendaId") Long agendaId,
                                   @AuthenticationPrincipal CustomUserDetails customUserDetails);
}
