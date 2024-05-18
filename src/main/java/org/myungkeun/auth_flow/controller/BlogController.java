package org.myungkeun.auth_flow.controller;

import lombok.RequiredArgsConstructor;
import org.myungkeun.auth_flow.dto.request.RegisterBlogRequest;
import org.myungkeun.auth_flow.dto.request.UpdateBlogRequest;
import org.myungkeun.auth_flow.dto.response.BaseResponse;
import org.myungkeun.auth_flow.entity.Blog;
import org.myungkeun.auth_flow.service.BlogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/blog")
public class BlogController {
    private final BlogService blogService;

    @PostMapping()
    ResponseEntity<BaseResponse<Blog>> register(
            @RequestBody RegisterBlogRequest request
    ) {
        Blog result = blogService.registerBlog(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new BaseResponse<>(result, HttpStatus.CREATED.value(), "게시물이 생성되었습니다."));
    }

    @GetMapping("/{id}")
    ResponseEntity<BaseResponse<Object>> getBlogById(
            @PathVariable(name = "id") Long id
    ) {
        Object result = blogService.getBlogById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(result, HttpStatus.OK.value(), "게시물을 가져왔습니다."));
    }

    @GetMapping("/all")
    ResponseEntity<BaseResponse<List<Blog>>> getAllBlog() {
        List<Blog> result = blogService.getAllBlog();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(result, HttpStatus.OK.value(), "게시물을 가져왔습니다."));
    }

    @PutMapping("/{id}")
    ResponseEntity<BaseResponse<Blog>> updateBlogByid(
            @PathVariable(name = "id") Long id,
            @RequestBody UpdateBlogRequest request
    ) {
        Blog reslut = blogService.updateBlogById(id, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(reslut, HttpStatus.OK.value(), "게시물을 수정하였습니다."));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<BaseResponse<String>> deleteBlogById(
            @PathVariable(name = "id") Long id
    ) {
        String result = blogService.deleteBlogById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(result, HttpStatus.OK.value(), "게시물이 삭제되었습니다."));
    }
}
