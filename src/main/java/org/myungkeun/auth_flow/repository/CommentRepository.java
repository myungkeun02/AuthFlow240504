package org.myungkeun.auth_flow.repository;

import org.myungkeun.auth_flow.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBlogId(Long blogId);
}
