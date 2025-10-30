package com.loopers.domain.user;

import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import static com.loopers.support.fixture.UserFixtures.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserServiceIntegrationTest {
    @Autowired
    private UserService userService;

    @MockitoSpyBean
    private UserJpaRepository userJpaRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("회원가입 시,")
    @Nested
    class Signup {
        @DisplayName("이미 가입된 ID로 시도 시 중복 예외가 발생한다.")
        @Test
        void register_failsWhenLoginIdExists() {
            // arrange
            doReturn(true).when(userJpaRepository).existsByLoginId("limjaein");
            User duplicateUser = User.create("limjaein", validPassword(), validEmail(), validBirthDate(), validGender());

            // act + assert
            assertThatThrownBy(() -> userService.register(duplicateUser))
                    .isInstanceOf(CoreException.class)
                    .hasMessageContaining("이미 존재하는 ID입니다.")
                    .extracting("errorType")
                    .isEqualTo(ErrorType.CONFLICT);

            verify(userJpaRepository, times(1)).existsByLoginId("limjaein");
            verify(userJpaRepository, never()).save(argThat(u -> "limjaein".equals(u.getLoginId())));
        }

        @DisplayName("User 저장이 수행된다 (Spy 검증)")
        @Test
        void register_whenInputIsValid() {
            // arrange
            User user = createValidUser();

            // act
            User savedUser = userService.register(user);

            // assert
            verify(userJpaRepository, times(1)).save(any(User.class));

            User persistedUser = userJpaRepository.findById(savedUser.getId()).orElseThrow();
            assertEquals(user.getLoginId(), persistedUser.getLoginId());

        }
    }


    @DisplayName("내 정보 조회 시,")
    @Nested
    class GetMe {
        @DisplayName("해당 ID의 회원이 존재할 경우, 회원 정보가 반환된다.")
        @Test
        void returnUser_whenLoginIdExists() {
            // arrange
            User user = User.create("limjaein", validPassword(), validEmail(), validBirthDate(), validGender());
            userService.register(user);

            // act
            User foundUser = userService.findByLoginId("limjaein").orElse(null);

            // assert

            verify(userJpaRepository, times(1)).findByLoginId("limjaein");

            assertThat(foundUser).isNotNull();
            assertAll(
                    () -> assertThat(foundUser.getLoginId()).isEqualTo(user.getLoginId()),
                    () -> assertThat(foundUser.getEmail()).isEqualTo(user.getEmail()),
                    () -> assertThat(foundUser.getBirthDate()).isEqualTo(user.getBirthDate()),
                    () -> assertThat(foundUser.getGender()).isEqualTo(user.getGender())
            );
        }

        @DisplayName("해당 ID의 회원이 존재하지 않을 경우, null이 반환된다.")
        @Test
        void returnNull_whenLoginIdNotExists() {
            // arrange

            // act
            User foundUser = userService.findByLoginId("limjaein").orElse(null);

            // assert

            verify(userJpaRepository, times(1)).findByLoginId("limjaein");
            assertThat(foundUser).isNull();
        }
    }
}
