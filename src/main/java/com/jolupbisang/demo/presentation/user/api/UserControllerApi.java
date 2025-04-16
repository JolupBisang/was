package com.jolupbisang.demo.presentation.user.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "사용자", description = "사용자 관련 API")
public interface UserControllerApi {

    @Operation(summary = "사용자 정보 조회", description = "이메일로 사용자 정보를 조회합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "조회 성공", value = """
                                        {
                                            "message": "회원 조회 성공",
                                            "data": {
                                                "id": 200001,
                                                "email": "oneyoung0623@gmail.com",
                                                "nickname": "정원영"
                                            }
                                        }
                                    """),
                    })
            ),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "조회 실패", value = """
                                        {
                                            "message": "존재하지 않는 회원입니다.",
                                            "errorId": "bac4f73e-07e8-41b3-86cb-075afa787bee",
                                            "errors": null
                                        }
                                    """),
                    })
            ),
    })
    ResponseEntity<?> getUserInfo(
            @Parameter(description = "사용자 이메일", required = true, example = "user@example.com")
            @PathVariable("email") String email);
}
