package com.loopers.interfaces.api.user;

import com.loopers.interfaces.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User V1 API", description = "Loopers 예시 API 입니다.")
public interface UserV1ApiSpec {

    @Operation(
        summary = "회원 가입",
        description = "유저 정보로 회원 가입"
    )

    ApiResponse<UserV1Dto.SignupResponse> signup(
        @Schema(name = "회원 가입 요청", description = "회원 가입에 필요한 정보")
        UserV1Dto.SignupRequest request
    );
}
