package org.myungkeun.auth_flow.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.myungkeun.auth_flow.entity.Address;
import org.myungkeun.auth_flow.entity.Blog;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class AllAddressResponse {
    private List<Address> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
