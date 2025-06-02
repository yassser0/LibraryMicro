package com.booknet.user_service.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.booknet.user_service.Entity.User; 

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}