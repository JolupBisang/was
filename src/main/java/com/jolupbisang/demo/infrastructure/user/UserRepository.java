package com.jolupbisang.demo.infrastructure.user;

import com.jolupbisang.demo.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long userId);

    Optional<User> findByEmail(String email);

    List<User> findByEmailIn(List<String> emails);
}
