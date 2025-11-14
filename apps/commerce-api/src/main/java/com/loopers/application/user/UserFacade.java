package com.loopers.application.user;

import com.loopers.domain.user.User;
import com.loopers.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class UserFacade {
    private final UserService userService;

    @Transactional
    public UserInfo signup(User user) {
        User savedUser = userService.register(user);
        return UserInfo.from(savedUser);
    }

    @Transactional(readOnly = true)
    public UserInfo getUser(Long id) {
        User user = userService.findById(id);
        return UserInfo.from(user);
    }
}
