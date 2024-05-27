package org.myungkeun.auth_flow.service.Impl;

import lombok.RequiredArgsConstructor;
import org.myungkeun.auth_flow.dto.response.AllBlogResponse;
import org.myungkeun.auth_flow.dto.request.RegisterBlogRequest;
import org.myungkeun.auth_flow.dto.request.UpdateBlogRequest;
import org.myungkeun.auth_flow.entity.Blog;
import org.myungkeun.auth_flow.entity.Member;
import org.myungkeun.auth_flow.exception.NotFoundException;
import org.myungkeun.auth_flow.repository.BlogRepository;
import org.myungkeun.auth_flow.repository.MemberRepository;
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
    private final MemberRepository memberRepository;
    private final CacheManager cacheManager;

    @Override
    public Blog registerBlog(RegisterBlogRequest request) {
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new NotFoundException("해당 멤버를 찾을수 없습니다."));
        Blog blog = Blog.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .description(request.getDescription())
                .image(request.getImage())
                .member(member)
                .build();
        Blog result = blogRepository.save(blog);
        cacheManager.deleteValues("blogs" + member.getId());
        cacheManager.deleteValues("blogs");
        return result;
    }

    @Override
    public Object getBlogById(Long id) {
        Object redis = cacheManager.getBlog(id);

        if (redis == null) {
            Blog rdbResponse = blogRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("게시글을 찾을수 없습니다."));
            cacheManager.saveBlog(id, rdbResponse, Duration.ofMillis(3600000));
            System.out.println("repository");
            return rdbResponse;
        }
        System.out.println("redis");
        return redis;
    }
    public Blog updateBlogById(Long id, UpdateBlogRequest request) {
        Blog oldBlog = blogRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 ID의 게시물을 찾지 못했습니다."));

        boolean isUpdated = false;

        if (request.getTitle() != null && !request.getTitle().equals(oldBlog.getTitle())) {
            oldBlog.setTitle(request.getTitle());
            isUpdated = true;
        }

        if (request.getContent() != null && !request.getContent().equals(oldBlog.getContent())) {
            oldBlog.setContent(request.getContent());
            isUpdated = true;
        }

        if (request.getDescription() != null && !request.getDescription().equals(oldBlog.getDescription())) {
            oldBlog.setDescription(request.getDescription());
            isUpdated = true;
        }

        if (request.getImage() != null && !request.getImage().equals(oldBlog.getImage())) {
            oldBlog.setImage(request.getImage());
            isUpdated = true;
        }

        if (isUpdated) {
            Blog result = blogRepository.save(oldBlog);
            cacheManager.deleteValues("blogs:" + oldBlog.getMember().getId());
            cacheManager.deleteValues(id.toString());
            cacheManager.deleteValues("blogs");
            return result;
        } else {
            return oldBlog;
        }
    }

    @Override
    public String deleteBlogById(Long id) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 id의 게시물을 찾지 못했습니다."));
        blogRepository.delete(blog);
        cacheManager.deleteValues("blogs"+blog.getMember().getId());
        cacheManager.deleteValues(id.toString());
        cacheManager.deleteValues("blogs");
        return "deleted";
    }

    @Override
    public AllBlogResponse getAllBlog(int pageNo, int pageSize, String sortBy, String sortDir) {
        return getBlogResponse(pageNo, pageSize, sortBy, sortDir, null);
    }

    @Override
    public AllBlogResponse getAllBlogByMemberId(Long memberId, int pageNo, int pageSize, String sortBy, String sortDir) {
        return getBlogResponse(pageNo, pageSize, sortBy, sortDir, memberId);
    }

    private AllBlogResponse getBlogResponse(int pageNo, int pageSize, String sortBy, String sortDir, Long memberId) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        String cacheKey = memberId == null ? "blogs" : "blogs" + memberId;
        List<Blog> redisBlogs = cacheManager.getAllBlog(cacheKey);

        if (redisBlogs == null) {
            List<Blog> rdbBlogs = memberId == null ? blogRepository.findAll() : blogRepository.findAllByMember(
                    memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException("해당 멤버의 게시물을 찾을 수 없습니다."))
            );
            Page<Blog> rdbResponse = new PageImpl<>(rdbBlogs, pageable, rdbBlogs.size());
            cacheManager.save(cacheKey, rdbBlogs, Duration.ofMillis(3600000));
            System.out.println("blogRepository");
            return createAllBlogResponse(rdbResponse);
        }

        System.out.println("redis");
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), redisBlogs.size());
        List<Blog> pagedList = redisBlogs.subList(start, end);
        Page<Blog> blogs = new PageImpl<>(pagedList, pageable, redisBlogs.size());
        return createAllBlogResponse(blogs);
    }

    private AllBlogResponse createAllBlogResponse(Page<Blog> blogPage) {
        return AllBlogResponse.builder()
                .content(blogPage.getContent())
                .pageSize(blogPage.getSize())
                .pageNo(blogPage.getNumber() + 1)
                .totalPages(blogPage.getTotalPages())
                .totalElements(blogPage.getTotalElements())
                .last(blogPage.isLast())
                .build();
    }
}
