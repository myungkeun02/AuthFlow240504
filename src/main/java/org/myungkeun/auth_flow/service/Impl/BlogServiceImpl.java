package org.myungkeun.auth_flow.service.Impl;

import lombok.RequiredArgsConstructor;
import org.myungkeun.auth_flow.dto.request.AllBlogResponse;
import org.myungkeun.auth_flow.dto.request.RegisterBlogRequest;
import org.myungkeun.auth_flow.dto.request.UpdateBlogRequest;
import org.myungkeun.auth_flow.entity.Blog;
import org.myungkeun.auth_flow.exception.NotFoundException;
import org.myungkeun.auth_flow.repository.BlogRepository;
import org.myungkeun.auth_flow.service.BlogService;
import org.myungkeun.auth_flow.util.CacheManager;
import org.springframework.data.domain.*;
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
    // 1. redis에 blogs 데이터가 있는지 확인 후 있을 경우 redis에서 데이터를 가져와 페이지네이션
    // 2. redis에 blogs 데이터가 없을경우 rdb에서 데이터를 가져와 페이지네이션 이떄 가져온 데이터를 redis에 등록한다.
    public AllBlogResponse getAllBlog(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo-1, pageSize, sort);

        List<Blog> redisBlogs = cacheManager.getAllBlog("blogs");
        if (redisBlogs == null) {
            List<Blog> rdbBlogs = blogRepository.findAll();
            Page<Blog> rdbResponse = blogRepository.findAll(pageable);
            List<Blog> contents = rdbResponse.getContent();
            AllBlogResponse response = AllBlogResponse.builder()
                    .content(contents)
                    .pageSize(rdbResponse.getSize())
                    .pageNo(rdbResponse.getNumber()+1)
                    .totalPages(rdbResponse.getTotalPages())
                    .totalElements(rdbResponse.getTotalElements())
                    .last(rdbResponse.isLast())
                    .build();
            cacheManager.save("blogs",rdbBlogs, Duration.ofDays(3600000));
            System.out.println("blogRepository");
            return response;
        }
        System.out.println("redis");

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), redisBlogs.size());
        List<Blog> pagedList = redisBlogs.subList(start, end);

        Page<Blog> blogs = new PageImpl<>(pagedList, pageable, redisBlogs.size());
        AllBlogResponse response = AllBlogResponse.builder()
                .content(blogs.getContent())
                .pageSize(blogs.getSize())
                .pageNo(blogs.getNumber()+1)
                .totalPages(blogs.getTotalPages())
                .totalElements(blogs.getTotalElements())
                .last(blogs.isLast())
                .build();
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
