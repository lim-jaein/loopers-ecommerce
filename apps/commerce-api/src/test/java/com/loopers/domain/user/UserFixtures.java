package com.loopers.domain.user;

public final class UserFixtures {

    private UserFixtures() {} // 인스턴스 생성 방지

    public static String validLoginId() { return "limjaein"; }
    public static String validPassword() { return "limjaein!@3"; }
    public static String validEmail() { return "limjaein@google.com"; }
    public static String validBirthDate() { return "1996-11-27"; }

}
