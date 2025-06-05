package com.jolupbisang.demo.presentation.meetingUser.api;

import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import com.jolupbisang.demo.presentation.meetingUser.dto.request.ParticipantAddReq;
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

@Tag(name = "회의 참여자", description = "회의 참여자 관련 API")
public interface MeetingUserControllerApi {

    @Operation(summary = "회의 참여자 추가", description = "회의에 새로운 참여자를 추가합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "참여자 추가 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "추가 성공", value = """
                                        {
                                            "message": "회의 참여자 추가 성공",
                                            "data": null
                                        }
                                    """),
                    })),
            @ApiResponse(responseCode = "400", description = "추가 실패",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "추가 실패 - 완료된 회의", value = """
                                        {
                                            "message": "완료되었거나 취소된 회의에는 참여자를 추가할 수 없습니다.",
                                            "errorId": "7b5c9308-9751-4055-82de-663bc3e85ce8",
                                            "errors": null
                                        }
                                    """),
                    })),
            @ApiResponse(responseCode = "403", description = "권한 없음 (호스트가 아님)",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "추가 실패 - 권한 없음", value = """
                                        {
                                            "message": "해당 작업은 회의 리더만 수행할 수 있습니다.",
                                            "errorId": "8f8d1814-d7aa-48cc-b114-e8ea792d579f",
                                            "errors": null
                                        }
                                    """),
                    })),
            @ApiResponse(responseCode = "422", description = "DTO 검증 실패",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "추가 실패 - DTO 검증 실패", value = """
                                        {
                                            "message": "잘못된 입력입니다.",
                                            "errorId": "e5ea6cfe-f7ab-4446-9f74-5666ec885099",
                                            "errors": {
                                                "emails": "참여자 이메일 목록은 필수입니다."
                                            }
                                        }
                                    """),
                            @ExampleObject(name = "추가 실패 - DTO 내 배열 요소 검증 실패", value = """
                                        {
                                            "message": "잘못된 입력입니다.",
                                            "errorId": "825c9df9-ee29-4f51-99a9-30c11bfcdbcd",
                                            "errors": {
                                                "emails[0]": "올바른 이메일 형식이어야 합니다."
                                            }
                                        }
                                    """),
                    }))
    })
    ResponseEntity<?> addMeetingUser(@PathVariable("meetingId") Long meetingId,
                                     @RequestBody @Valid ParticipantAddReq participantAddReq,
                                     @AuthenticationPrincipal CustomUserDetails customUserDetails);

    @Operation(summary = "회의 참여자 제거", description = "호스트가 회의에서 참여자를 제거합니다. WAITING 상태에서만 가능하며, 호스트는 제거할 수 없습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "참여자 제거 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "제거 성공", value = """
                                        {
                                            "message": "회의 참여자 제거 성공",
                                            "data": null
                                        }
                                    """),
                    })),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "제거 실패 - WAITING 상태 아님", value = """
                                        {
                                            "message": "대기 중인 회의에서만 참여자를 제거할 수 있습니다.",
                                            "errorId": "28bdb5ac-5b19-4993-8e58-2baf3e73f03f",
                                            "errors": null
                                        }
                                    """),
                    })),
            @ApiResponse(responseCode = "403", description = "권한 없음 (호스트가 아님)",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "제거 실패 - 권한 없음", value = """
                                        {
                                            "message": "해당 작업은 회의 리더만 수행할 수 있습니다.",
                                            "errorId": "84fc50ec-71a6-440e-90ff-1e5304b71c99",
                                            "errors": null
                                        }
                                    """),
                    })),
            @ApiResponse(responseCode = "404", description = "회의 또는 참여자를 찾을 수 없음",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "제거 실패 - 참여자 아님", value = """
                                        {
                                            "message": "회의에 참여하지 않은 사용자입니다.",
                                            "errorId": "68c9cfae-5d7e-485c-ab9d-e4250693e5e4",
                                            "errors": null
                                        }
                                    """),
                    })),
            @ApiResponse(responseCode = "400", description = "호스트를 제거하려 함",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "제거 실패 - 호스트 제거 시도", value = """
                                        {
                                            "message": "호스트는 회의에서 제거할 수 없습니다.",
                                            "errorId": "20d0c117-9716-40de-be9d-04a8571da588",
                                            "errors": null
                                        }
                                    """),
                    }))
    })
    ResponseEntity<Void> removeMeetingUser(
            @PathVariable Long meetingId,
            @PathVariable Long participantUserId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    );
} 
