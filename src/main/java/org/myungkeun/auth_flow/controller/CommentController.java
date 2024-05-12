package org.myungkeun.auth_flow.controller;

import lombok.RequiredArgsConstructor;
import org.myungkeun.auth_flow.dto.request.CreateCommentRequest;
import org.myungkeun.auth_flow.dto.request.UpdateCommentRequest;
import org.myungkeun.auth_flow.dto.response.BaseResponse;
import org.myungkeun.auth_flow.entity.Comment;
import org.myungkeun.auth_flow.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comment")
public class CommentController {
    private final CommentService commentService;
    @PostMapping("/{blogId}")
    ResponseEntity<BaseResponse<Comment>> createComment(
            @PathVariable(name = "blogId") Long blogId,
            @RequestBody CreateCommentRequest request
    ) {
        Comment comment = commentService.createComment(blogId, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(comment, HttpStatus.OK.value(), "코멘트가 작성되었습니다."));
    }

    @GetMapping("/{blogId}/all")
    ResponseEntity<BaseResponse<List<Comment>>> getAllComment(
            @PathVariable(name = "blogId") Long blogId
    ) {
        List<Comment> result = commentService.getAllComment(blogId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(result, HttpStatus.OK.value(), "해당 게시물의 모든 코멘트를 가져옵니다."));
    }

    @GetMapping("/{id}")
    ResponseEntity<BaseResponse<Comment>> getCommentById(
            @PathVariable(name = "id") Long id
    ) {
        Comment result = commentService.getCommentById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(result, HttpStatus.OK.value(), "댓글을 가져왔습니다."));
    }

    @PutMapping("/{blogId}/{commentId}")
    ResponseEntity<BaseResponse<Comment>> updateCommentById(
            @PathVariable(name = "blogId") Long blogId,
            @PathVariable(name = "commentId") Long commentId,
            @RequestBody UpdateCommentRequest request
    ) {
        Comment result = commentService.UpdateCommentById(blogId,  commentId, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(result, HttpStatus.OK.value(), "댓글을 업데이트 했습니다."));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<BaseResponse<String>> deleteCommentById(
            @PathVariable(name = "id") Long id
    ) {
        String result = commentService.deleteCommentById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(result, HttpStatus.OK.value(), "댓글을 삭제하였습니다."));
    }
}
