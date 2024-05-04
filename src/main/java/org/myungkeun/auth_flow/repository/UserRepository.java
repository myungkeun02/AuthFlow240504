package org.myungkeun.auth_flow.repository;

import org.myungkeun.auth_flow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmailId(final String emailId);
    Optional<User> findByEmailId(String emailId);
}
