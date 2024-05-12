package org.myungkeun.auth_flow.service.Impl;

import lombok.RequiredArgsConstructor;
import org.myungkeun.auth_flow.dto.request.CreateCommentRequest;
import org.myungkeun.auth_flow.dto.request.UpdateCommentRequest;
import org.myungkeun.auth_flow.entity.Blog;
import org.myungkeun.auth_flow.entity.Comment;
import org.myungkeun.auth_flow.exception.BadRequestException;
import org.myungkeun.auth_flow.repository.BlogRepository;
import org.myungkeun.auth_flow.repository.CommentRepository;
import org.myungkeun.auth_flow.service.CommentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final BlogRepository blogRepository;

    @Override
    public Comment createComment(Long blogId, CreateCommentRequest request) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow();
        Comment comment = Comment.builder()
                .name(request.getName())
                .email(request.getEmail())
                .body(request.getBody())
                .blog(blog)
                .build();
        Comment result = commentRepository.save(comment);
        return result;
    }

    @Override
    public List<Comment> getAllComment(Long blogId) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow();
        return commentRepository.findByBlogId(blogId);
    }

    @Override
    public Comment getCommentById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow();
    }

    @Override
    public Comment UpdateCommentById(Long blogId, Long commentId, UpdateCommentRequest request) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow();

        if (!comment.getBlog().getId().equals(blog.getId())) {
            throw new BadRequestException("블로그 id가 일치하지않음");
        }
        comment.setName(request.getName());
        comment.setEmail(request.getEmail());
        comment.setBody(request.getBody());
        Comment result = commentRepository.save(comment);
        return result;
    }

    @Override
    public String deleteCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow();
        commentRepository.delete(comment);
        return "deleted";
    }
}
