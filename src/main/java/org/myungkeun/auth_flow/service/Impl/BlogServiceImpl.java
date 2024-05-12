package org.myungkeun.auth_flow.service.Impl;

import lombok.RequiredArgsConstructor;
import org.myungkeun.auth_flow.dto.request.RegisterBlogRequest;
import org.myungkeun.auth_flow.dto.request.UpdateBlogRequest;
import org.myungkeun.auth_flow.entity.Blog;
import org.myungkeun.auth_flow.exception.NotFoundException;
import org.myungkeun.auth_flow.repository.BlogRepository;
import org.myungkeun.auth_flow.service.BlogService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class BlogServiceImpl implements BlogService {
    private final BlogRepository blogRepository;

    @Override
    public Blog updateBlogById(Long id, UpdateBlogRequest request) {
        Blog oldBlog = blogRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 ID의 게시물을 찾지 못했습니다."));

        oldBlog.setTitle(request.getTitle());
        oldBlog.setContent(request.getContent());
        oldBlog.setDescription(request.getDescription());
        oldBlog.setImage(request.getImage());
        Blog result = blogRepository.save(oldBlog);
        return result;
    }

    @Override
    public List<Blog> getAllBlog() {
        List<Blog> result = blogRepository.findAll();
        return result;
    }

    @Override
    public Blog getBlogById(Long id) {
        Blog result = blogRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 ID의 게시물을 찾지 못했습니다."));
        return result;
    }

    @Override
    public Blog registerBlog(RegisterBlogRequest request) {
        Blog blog = Blog.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .description(request.getDescription())
                .image(request.getImage())
                .build();
        Blog result = blogRepository.save(blog);
        return result;
    }

    @Override
    public String deleteBlogById(Long id) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 id의 게시물을 찾지 못했습니다."));
        blogRepository.delete(blog);
        return "deleted";
    }
}
