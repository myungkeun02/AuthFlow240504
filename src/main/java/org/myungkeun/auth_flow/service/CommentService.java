package org.myungkeun.auth_flow.service;

import org.myungkeun.auth_flow.dto.request.CreateCommentRequest;
import org.myungkeun.auth_flow.dto.request.UpdateCommentRequest;
import org.myungkeun.auth_flow.entity.Comment;

import java.util.List;

public interface CommentService {
    Comment createComment(Long blogId, CreateCommentRequest request);

    List<Comment> getAllComment(Long blogId);

    Comment getCommentById(Long id);

    Comment UpdateCommentById(Long blogId, Long commentId, UpdateCommentRequest request);

    String deleteCommentById(Long id);
}
