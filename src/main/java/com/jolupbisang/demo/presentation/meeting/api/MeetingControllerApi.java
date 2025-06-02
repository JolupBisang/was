package com.jolupbisang.demo.presentation.meeting.api;

import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import com.jolupbisang.demo.presentation.meeting.dto.request.MeetingReq;
import com.jolupbisang.demo.presentation.meeting.dto.request.MeetingStatusUpdateReq;
import com.jolupbisang.demo.presentation.meeting.dto.request.MeetingUpdateReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "회의", description = "회의 관련 API")
public interface MeetingControllerApi {

    @Operation(summary = "회의 생성", description = "새로운 회의를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회의 생성 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "생성 성공", value = """
                                        {
                                            "message": "회의 생성 성공",
                                            "data": 5
                                        }
                                    """),
                    })),
            @ApiResponse(responseCode = "422", description = "DTO 검증 실패",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "생성 실패 (전체 요소 검증 실패)", value = """
                                        {
                                            "message": "잘못된 입력입니다.",
                                            "errorId": "39b4794e-1f00-4c21-a5b9-a0ce481369a7",
                                            "errors": {
                                                "agendas": "회의 안건 목록은 필수입니다.",
                                                "scheduledStartTime": "회의 시작 시간은 필수입니다.",
                                                "restInterval": "회의 휴식 시간은 필수입니다.",
                                                "location": "회의 장소는 필수입니다.",
                                                "title": "회의 제목은 필수입니다.",
                                                "participants": "참여자 목록은 필수입니다.",
                                                "targetTime": "회의 목표 시간은 필수입니다."
                                            }
                                        }
                                    """),
                            @ExampleObject(name = "생성 실패 (배열 요소 검증 실패)", value = """
                                        {
                                            "message": "잘못된 입력입니다.",
                                            "errorId": "0d5b6ee9-2ecd-4299-bc0f-1ce17dfc34a4",
                                            "errors": {
                                                "agendas[0]": "회의 안건은 1글자 이상이어야 합니다.",
                                                "participants[0]": "참여자는 이메일 형식이어야 합니다."
                                            }
                                        }
                                    """),
                    })),
    })
    ResponseEntity<?> createMeeting(
            @Parameter(description = "회의 생성 요청 정보", required = true)
            @Valid @RequestBody MeetingReq meetingReq,
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails userDetails);

    @Operation(summary = "회의 상세 조회", description = "특정 회의의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회의 조회 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "조회 성공", value = """
                                        {
                                              "message": "회의 조회 성공",
                                              "data": {
                                                  "meetingId": 2,
                                                  "title": "프로젝트 킥오프 회의",
                                                  "location": "서울 본사 회의실 A",
                                                  "scheduledStartTime": "2025-03-27T10:00:00",
                                                  "targetTime": 60,
                                                  "restInterval": 60,
                                                  "restDuration": 0,
                                                  "meetingStatus": "IN_PROGRESS",
                                                  "participants": [
                                                      {
                                                          "userId": 1,
                                                          "email": "oneyoung0623@gmail.com"
                                                      }
                                                  ],
                                                  "isHost": true
                                              }
                                          }
                                    """),
                    })),
            @ApiResponse(responseCode = "403", description = "권한 없음 또는 없는 회의",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "조회 실패", value = """
                                        {
                                            "message": "해당 회의의 참여자가 아닙니다.",
                                            "errorId": "5d458520-6cd7-404f-ab91-234bd05fbb66",
                                            "errors": null
                                        }
                                    """),
                    }))
    })
    ResponseEntity<?> getMeetingDetail(
            @Parameter(description = "회의 ID", required = true, example = "1")
            @PathVariable Long meetingId,
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails userDetails);

    @Operation(summary = "월별 회의 목록 조회", description = "년도와 월을 기준으로 회의 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회의 목록 조회 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "조회 성공", value = """
                                        {
                                              "message": "회의 목록 조회 성공",
                                              "data": {
                                                  "meetings": [
                                                      {
                                                          "id": 2,
                                                          "title": "프로젝트 킥오프 회의",
                                                          "location": "서울 본사 회의실 A",
                                                          "scheduledStartTime": "2025-03-27T10:00",
                                                          "targetTime": 60,
                                                          "status": "IN_PROGRESS"
                                                      },
                                                      {
                                                          "id": 1,
                                                          "title": "프로젝트 킥오프 회의22",
                                                          "location": "서울 본사 회의실 A",
                                                          "scheduledStartTime": "2025-03-27T10:00",
                                                          "targetTime": 60,
                                                          "status": "IN_PROGRESS"
                                                      }
                                                  ]
                                              }
                                          }
                                    """),
                    })),
            @ApiResponse(responseCode = "400", description = "잘못된 날짜 형식",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "조회 실패", value = """
                                        {
                                            "message": "잘못된 회의 날짜 형식입니다.",
                                            "errorId": "a4016a03-5ed0-4915-856f-a6374854c0d4",
                                            "errors": null
                                        }
                                    """)
                    })),
    })
    ResponseEntity<?> getMeetings(
            @Parameter(description = "연도", required = true, example = "2023")
            @RequestParam("year") Integer year,
            @Parameter(description = "월", required = true, example = "7")
            @RequestParam("month") Integer month,
            @AuthenticationPrincipal CustomUserDetails userDetails);

    @Operation(summary = "회의 상태 변경", description = "회의 상태를 변경합니다. 요청 바디의 'targetStatus' 필드에 'IN_PROGRESS', 'COMPLETED', 'CANCELLED' 중 하나를 명시해야 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회의 상태 변경 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "상태 변경 성공", value = """
                                        {
                                            "message": "성공적으로 변경되었습니다.",
                                            "data": null
                                        }
                                    """)
                    })),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (예: 지원하지 않는 targetStatus, 회의 상태 규칙 위반, 필수 필드 누락)",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "잘못된 targetStatus", value = """
                                        {
                                            "message": "요청된 상태로 회의를 변경할 수 없거나 유효하지 않은 상태 값입니다.",
                                            "errorId": "846f8bed-a9ce-46c0-a229-40ad3a273b24",
                                            "errors": null
                                        }
                                    """),
                            @ExampleObject(name = "대기 상태 아님(IN_PROGRESS 요청 시)", value = """
                                        {
                                            "message": "대기중인 회의가 아닙니다.",
                                            "errorId": "435899bd-13c7-434d-9fbb-ff5816056147",
                                            "errors": null
                                        }
                                    """),
                            @ExampleObject(name = "진행중 상태 아님(COMPLETED 요청 시)", value = """
                                        {
                                            "message": "진행중인 회의가 아닙니다.",
                                            "errorId": "371c1d77-eb2e-432e-ae65-63400afafcd6",
                                            "errors": null
                                        }
                                    """),
                            @ExampleObject(name = "필수 필드 누락", value = """
                                        {
                                            "message": "잘못된 입력입니다.",
                                            "errorId": "aad60f0b-76bc-4b29-8db1-6f28144f7151",
                                            "errors": {
                                                "targetStatus": "변경할 회의 상태는 필수입니다."
                                            }
                                        }
                                    """)
                    })),
            @ApiResponse(responseCode = "403", description = "권한 없음 (예: 회의 리더가 아님)",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "상태 변경 실패 - 권한 없음", value = """
                                        {
                                            "message": "해당 작업은 회의 리더만 수행할 수 있습니다.",
                                            "errorId": "85cad392-249f-4613-ab7c-cd069fa724b1",
                                            "errors": null
                                        }
                                    """)
                    })),
            @ApiResponse(responseCode = "404", description = "회의를 찾을 수 없음",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "상태 변경 실패 - 회의 없음", value = """
                                        {
                                            "message": "존재하지 않는 회의입니다.",
                                            "errorId": "1353e6d9-02dd-4b80-9e7a-77e46392798b",
                                            "errors": null
                                        }
                                    """)
                    }))
    })
    ResponseEntity<?> updateMeetingStatus(
            @Parameter(description = "회의 ID", required = true, example = "1")
            @PathVariable Long meetingId,
            @Valid @RequestBody MeetingStatusUpdateReq statusUpdateReq,
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails userDetails);

    @Operation(summary = "회의 정보 수정", description = "회의 정보를 수정합니다. 회의 리더만 수정 가능하며, 종료된 회의는 수정할 수 없습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회의 정보 수정 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "수정 성공", value = """
                                        {
                                            "message": "회의 정보가 성공적으로 수정되었습니다.",
                                            "data": null
                                        }
                                    """)
                    })),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (예: 필수 필드 누락, 종료된 회의)",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "필수 필드 누락", value = """
                                        {
                                            "message": "잘못된 입력입니다.",
                                            "errorId": "46455254-8092-4fb8-99b2-2bfb8defd1f2",
                                            "errors": {
                                                "agendas": "회의 안건 목록은 필수입니다.",
                                                "scheduledStartTime": "회의 시작 시간은 필수입니다.",
                                                "restInterval": "회의 휴식 시간은 필수입니다.",
                                                "location": "회의 장소는 필수입니다.",
                                                "title": "회의 제목은 필수입니다.",
                                                "restDuration": "회의 휴식 시간 길이는 필수입니다.",
                                                "targetTime": "회의 목표 시간은 필수입니다."
                                            }
                                        }
                                    """),
                            @ExampleObject(name = "종료되었거나 취소된 회의", value = """
                                        {
                                            "message": "종료되었거나 취소된 회의는 수정할 수 없습니다.",
                                            "errorId": "af34d4f0-4ec7-40a6-b8ff-ad75f84efb4d",
                                            "errors": null
                                        }
                                    """)
                    })),
            @ApiResponse(responseCode = "403", description = "권한 없음 (예: 회의 리더가 아님)",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "수정 실패 - 권한 없음", value = """
                                        {
                                            "message": "해당 작업은 회의 리더만 수행할 수 있습니다.",
                                            "errorId": "798640f6-a807-417c-bb75-67116f55ee58",
                                            "errors": null
                                        }
                                    """)
                    }))
    })
    ResponseEntity<?> updateMeeting(
            @Parameter(description = "회의 ID", required = true, example = "1")
            @PathVariable Long meetingId,
            @Parameter(description = "회의 수정 요청 정보", required = true)
            @Valid @RequestBody MeetingUpdateReq meetingUpdateReq,
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails userDetails);
}
