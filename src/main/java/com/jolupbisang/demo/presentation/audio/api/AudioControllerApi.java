package com.jolupbisang.demo.presentation.audio.api;

import com.jolupbisang.demo.application.audio.dto.AudioListResponse;
import com.jolupbisang.demo.global.response.SuccessResponse;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

public interface AudioControllerApi {

    @Operation(summary = "임베딩 오디오 전송", description = "목소리 인식을 위해서 임베딩 오디오를 보냅니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "임베딩 오디오 전송 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "전송 성공", value = """
                                        {
                                             "message": "전송 성공",
                                             "data": null
                                         }
                                    """)
                    })),
    })
    ResponseEntity<?> embeddingAudio(@RequestPart("audioFile") MultipartFile file,
                                     @Parameter(hidden = true)
                                     @AuthenticationPrincipal CustomUserDetails userDetails);

    @Operation(summary = "완료된 회의 오디오 목록 조회", description = "완료된 회의의 모든 참여자 오디오 파일에 대한 presigned URL 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "오디오 목록 조회 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "조회 성공", value = """
                                        {
                                            "message": "오디오 목록 조회 성공",
                                            "data": {
                                                "audioList": [
                                                    {
                                                        "userId": 1,
                                                        "presignedUrl": "https://bucket-silrok.s3.ap-northeast-2.amazonaws.com..."
                                                    },
                                                    {
                                                        "userId": 2,
                                                        "presignedUrl": "https://bucket-silrok.s3.ap-northeast-2.amazonaws.com..."
                                                    }
                                                ]
                                            }
                                        }
                                    """)
                    })),
            @ApiResponse(responseCode = "403", description = "회의 접근 권한 없음",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "권한 없음", value = """
                                        {
                                            "message": "해당 회의의 참여자가 아닙니다.",
                                            "errorId": "6b4b8956-f5a9-4d3a-9093-e8f488ca53ce",
                                            "errors": null
                                        }
                                    """)
                    })),
            @ApiResponse(responseCode = "400", description = "회의가 완료되지 않음",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "회의 미완료", value = """
                                        {
                                            "message": "완료된 회의가 아닙니다.",
                                            "errorId": "1ec70342-d2d4-4c7d-af74-65c5a0b897bb",
                                            "errors": null
                                        }
                                    """)
                    }))
    })
    ResponseEntity<SuccessResponse<AudioListResponse>> getCompletedMeetingAudioList(
            @Parameter(description = "회의 ID", required = true, example = "123")
            @PathVariable Long meetingId,
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails userDetails);
}
