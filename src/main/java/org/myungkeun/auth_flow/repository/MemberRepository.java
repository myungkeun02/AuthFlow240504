package org.myungkeun.auth_flow.repository;

import org.myungkeun.auth_flow.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.*;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    Boolean existsByEmail(String email);

    Boolean existsByNickname(String nickname);
}

// todo swagger , exists email, nickname
