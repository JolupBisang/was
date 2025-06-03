package com.jolupbisang.demo.presentation.audio.api;

import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
}
