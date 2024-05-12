package org.myungkeun.auth_flow.service;

import org.myungkeun.auth_flow.dto.request.RegisterBlogRequest;
import org.myungkeun.auth_flow.dto.request.UpdateBlogRequest;
import org.myungkeun.auth_flow.entity.Blog;

import java.util.List;

public interface BlogService {
    List<Blog> getAllBlog();
    Blog getBlogById(Long id);

    Blog registerBlog(RegisterBlogRequest request);

    Blog updateBlogById(Long id, UpdateBlogRequest request);

    String deleteBlogById(Long id);
}