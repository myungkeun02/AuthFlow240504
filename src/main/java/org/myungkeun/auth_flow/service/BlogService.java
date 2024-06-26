package org.myungkeun.auth_flow.service;

import org.myungkeun.auth_flow.dto.response.AllBlogResponse;
import org.myungkeun.auth_flow.dto.request.RegisterBlogRequest;
import org.myungkeun.auth_flow.dto.request.UpdateBlogRequest;
import org.myungkeun.auth_flow.entity.Blog;

public interface BlogService {
    AllBlogResponse getAllBlog(int pageNo, int pageSize, String sortBy, String sortDir);
    Object getBlogById(Long id);

    Blog registerBlog(RegisterBlogRequest request);

    Blog updateBlogById(Long id, UpdateBlogRequest request);

    String deleteBlogById(Long id);

    AllBlogResponse getAllBlogByMemberId(Long memberId, int pageNo, int pageSize, String sortBy, String sortDir);
}
