package com.loopers.domain.user;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import static com.loopers.domain.user.UserFixtures.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceIntegrationTest {
    @Autowired
    private UserService userService;

    @MockitoSpyBean
    private UserRepository userRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("회원가입 시,")
    @Nested
    class Join {
        @DisplayName("이미 가입된 ID로 시도 시 중복 예외가 발생한다.")
        @Test
        void register_failsWhenLoginIdExists() {
            // arrange
            doReturn(true).when(userRepository).existsByLoginId("limjaein");
            User duplicateUser = User.create("limjaein", validPassword(), validEmail(), validBirthDate());

            // act + assert
            assertThatThrownBy(() -> userService.register(duplicateUser))
                    .isInstanceOf(CoreException.class)
                    .hasMessageContaining("이미 존재하는 ID입니다.")
                    .extracting("errorType")
                    .isEqualTo(ErrorType.CONFLICT);

            verify(userRepository, times(1)).existsByLoginId("limjaein");
            verify(userRepository, never()).save(argThat(u -> "limjaein".equals(u.getLoginId())));
        }

        @DisplayName("User 저장이 수행된다 (Spy 검증)")
        @Test
        void register_whenInputIsValid() {
            // arrange
            User user = createValidUser();

            // act
            User savedUser = userService.register(user);

            // assert
            verify(userRepository, times(1)).save(any(User.class));

            User persistedUser = userRepository.find(savedUser.getId()).orElseThrow();
            assertEquals(user.getLoginId(), persistedUser.getLoginId());

        }
    }
}
