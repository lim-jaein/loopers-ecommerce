package com.loopers.domain.user;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User register(User user) {
        if(userRepository.existsByLoginId(user.getLoginId())) {
            throw new CoreException(ErrorType.CONFLICT, "이미 존재하는 ID입니다.");
        }

        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId);
    }

}
