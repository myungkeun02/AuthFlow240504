package org.myungkeun.auth_flow.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.myungkeun.auth_flow.entity.Blog;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class AllBlogResponse {
        private List<Blog> content;
        private int pageNo;
        private int pageSize;
        private long totalElements;
        private int totalPages;
        private boolean last;
}
