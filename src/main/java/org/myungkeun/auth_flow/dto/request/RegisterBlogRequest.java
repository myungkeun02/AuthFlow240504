package org.myungkeun.auth_flow.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class RegisterBlogRequest {
    private String title;
    private String content;
    private String description;
    private String image;
    private Long memberId;
}
