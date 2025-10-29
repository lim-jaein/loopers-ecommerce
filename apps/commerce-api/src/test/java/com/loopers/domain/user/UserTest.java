package com.loopers.domain.user;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static com.loopers.domain.user.UserFixtures.*;

@DisplayName("User 생성 시")
public class UserTest {

    @DisplayName("모든 필드가 유효하면 생성 성공한다.")
    @Test
    void createUser() {
        // act
        User result = createValidUser();

        // assert
        assertThat(result).isNotNull();
        assertThat(result.getLoginId()).isEqualTo(validLoginId());
        assertThat(result.getPassword()).isEqualTo(validPassword());
        assertThat(result.getEmail()).isEqualTo(validEmail());
        assertThat(result.getBirthDate()).isEqualTo(validBirthDate());
        assertThat(result.getGender()).isEqualTo(validGender());

    }

    @DisplayName("ID 가 영문 및 숫자 10자 이내 형식에 맞지 않으면 실패한다.")
    @Test
    void createUser_failsWhenIdHasInvalidFormat() {
        // arrange
        String invalidId = "아이디";

        // act
        CoreException result = assertThrows(CoreException.class, () ->
            User.create(invalidId, validPassword(), validEmail(), validBirthDate(), validGender()));

        // assert
        assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
    }


    @DisplayName("이메일이 xx@yy.zz 형식에 맞지 않으면 실패한다.")
    @Test
    void createUser_failsWhenEmailHasInvalidFormat() {
        // arrange
        String invalidEmail = "1234";

        // act
        CoreException result = assertThrows(CoreException.class, () ->
                User.create(validLoginId(), validPassword(), invalidEmail, validBirthDate(), validGender()));

        // assert
        assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
    }

    @DisplayName("생년월일이 yyyy-MM-dd 형식에 맞지 않으면 실패한다.")
    @Test
    void createUser_failsWhenBirthDateHasInvalidFormat() {
        // arrange
        String invalidBirthDate = "19961127";

        // act
        CoreException result = assertThrows(CoreException.class, () ->
                User.create(validLoginId(), validPassword(), validEmail(), invalidBirthDate, validGender()));

        // assert
        assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
    }
}
