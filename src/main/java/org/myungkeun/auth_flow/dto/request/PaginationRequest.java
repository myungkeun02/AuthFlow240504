package org.myungkeun.auth_flow.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class PaginationRequest {
    private int pageNo;
    private int pageSize;
    private String sortBy;
    private String sortDir;
}
