package com.loopers.domain.user;

import java.util.Optional;

public interface UserRepository {
    Optional<User> find(Long id);
    boolean existsByLoginId(String loginId);
    User save(User user);
}
