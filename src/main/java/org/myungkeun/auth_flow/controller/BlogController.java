package org.myungkeun.auth_flow.controller;

import lombok.RequiredArgsConstructor;
import org.myungkeun.auth_flow.dto.request.AllBlogResponse;
import org.myungkeun.auth_flow.dto.request.RegisterBlogRequest;
import org.myungkeun.auth_flow.dto.request.UpdateBlogRequest;
import org.myungkeun.auth_flow.dto.response.BaseResponse;
import org.myungkeun.auth_flow.entity.Blog;
import org.myungkeun.auth_flow.exception.NotFoundException;
import org.myungkeun.auth_flow.service.BlogService;
import org.myungkeun.auth_flow.util.AppConstants;
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
        try {
            Blog result = blogService.registerBlog(request);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new BaseResponse<>(result, HttpStatus.CREATED.value(), "게시물이 생성되었습니다."));
        } catch (NotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new BaseResponse<>(null, HttpStatus.NOT_FOUND.value(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseResponse<>(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }

    }

    @GetMapping("/{id}")
    ResponseEntity<BaseResponse<Object>> getBlogById(
            @PathVariable(name = "id") Long id
    ) {
        try {
            Object result = blogService.getBlogById(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BaseResponse<>(result, HttpStatus.OK.value(), "게시물을 가져왔습니다."));
        } catch (NotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new BaseResponse<>(null, HttpStatus.NOT_FOUND.value(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseResponse<>(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }

    }

    @PutMapping("/{id}")
    ResponseEntity<BaseResponse<Blog>> updateBlogByid(
            @PathVariable(name = "id") Long id,
            @RequestBody UpdateBlogRequest request
    ) {
        try {
            Blog reslut = blogService.updateBlogById(id, request);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BaseResponse<>(reslut, HttpStatus.OK.value(), "게시물을 수정하였습니다."));
        } catch (NotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new BaseResponse<>(null, HttpStatus.NOT_FOUND.value(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseResponse<>(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
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

    @GetMapping("/all")
    ResponseEntity<BaseResponse<AllBlogResponse>> getAllBlog(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        try {
            AllBlogResponse result = blogService.getAllBlog(pageNo, pageSize, sortBy, sortDir);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BaseResponse<>(result, HttpStatus.OK.value(), "게시물을 가져왔습니다."));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseResponse<>(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

    @GetMapping("/all/{memberId}")
    ResponseEntity<BaseResponse<AllBlogResponse>> getAllBlogByMemberId(
            @PathVariable(name = "memberId") Long memberId,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir

    ) {
        try {
            AllBlogResponse response = blogService.getAllBlogByMemberId(memberId, pageNo, pageSize, sortBy, sortDir);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BaseResponse<>(response, HttpStatus.OK.value(), "해당 멤버의 블로그를 모두 가져옵니다."));
        } catch (NotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new BaseResponse<>(null, HttpStatus.NOT_FOUND.value(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseResponse<>(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }
}
