package org.myungkeun.auth_flow.service.Impl;

import lombok.RequiredArgsConstructor;
import org.myungkeun.auth_flow.dto.request.RegisterBlogRequest;
import org.myungkeun.auth_flow.dto.request.UpdateBlogRequest;
import org.myungkeun.auth_flow.entity.Blog;
import org.myungkeun.auth_flow.exception.NotFoundException;
import org.myungkeun.auth_flow.repository.BlogRepository;
import org.myungkeun.auth_flow.service.BlogService;
import org.myungkeun.auth_flow.util.CacheManager;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor

public class BlogServiceImpl implements BlogService {
    private final BlogRepository blogRepository;
    private final CacheManager cacheManager;
    @Override
    public Blog updateBlogById(Long id, UpdateBlogRequest request) {
        Blog oldBlog = blogRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 ID의 게시물을 찾지 못했습니다."));

        oldBlog.setTitle(request.getTitle());
        oldBlog.setContent(request.getContent());
        oldBlog.setDescription(request.getDescription());
        oldBlog.setImage(request.getImage());
        Blog result = blogRepository.save(oldBlog);
        cacheManager.deleteValues(id.toString());
        cacheManager.deleteValues("blogs");
        return result;
    }

    @Override
    public List<Blog> getAllBlog() {
        List<Blog> response = cacheManager.getAllBlog("blogs");
        if ( response == null) {
            List<Blog> result = blogRepository.findAll();
            cacheManager.saveAllBlog("blogs", result, Duration.ofDays(3600000));
            System.out.println("blogRepository");
            return result;
        }
        System.out.println("redis");
        return response;
    }

    @Override
    public Object getBlogById(Long id) {
        Object redis = cacheManager.getBlog(id);

        if (redis == null) {
            Blog rdbResponse = blogRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("게시글을 찾을수 없습니다."));
            cacheManager.saveBlog(id, rdbResponse, Duration.ofDays(3600000));
            System.out.println("repository");
            return rdbResponse;
        }
        System.out.println("redis");
        return redis;
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
        cacheManager.deleteValues("blogs");
        return result;
    }

    @Override
    public String deleteBlogById(Long id) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 id의 게시물을 찾지 못했습니다."));
        blogRepository.delete(blog);
        cacheManager.deleteValues(id.toString());
        cacheManager.deleteValues("blogs");
        return "deleted";
    }
}
