package org.myungkeun.auth_flow.repository;

import org.myungkeun.auth_flow.entity.Blog;
import org.myungkeun.auth_flow.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    Optional<Blog> findById(Long id);

    List<Blog> findAllByMember(Member member);
}
