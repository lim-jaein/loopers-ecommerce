package com.loopers.support.fixture;

import com.loopers.domain.user.Gender;
import com.loopers.domain.user.User;

public final class UserFixtures {

    private UserFixtures() {} // 인스턴스 생성 방지

    public static String validLoginId() { return "limjaein"; }
    public static String validPassword() { return "limjaein!@3"; }
    public static String validEmail() { return "limjaein@google.com"; }
    public static String validBirthDate() { return "1996-11-27"; }
    public static Gender validGender() { return Gender.FEMALE; }

    public static User createValidUser() {
        return User.create(
                validLoginId(),
                validPassword(),
                validEmail(),
                validBirthDate(),
                validGender()
        );
    }
}
