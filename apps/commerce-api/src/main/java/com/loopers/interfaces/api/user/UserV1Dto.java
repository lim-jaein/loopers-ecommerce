package com.loopers.interfaces.api.user;

import com.loopers.application.user.UserInfo;
import com.loopers.domain.user.User;

public class UserV1Dto {
    public record SignupRequest(String loginId, String password, String email, String birthDate, String gender) {
        public User toEntity() {
            return User.create(loginId, password, email, birthDate, gender);
        }
    }
    public record SignupResponse(Long id, String loginId, String email, String birthDate, String gender) {
        public static SignupResponse from(UserInfo info) {
            return new SignupResponse(
                info.id(),
                info.loginId(),
                info.email(),
                info.birthDate(),
                info.gender()
            );
        }
    }
}
