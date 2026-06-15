package com.senhorcafe.urlshortner.user.repository;

import com.senhorcafe.urlshortner.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
