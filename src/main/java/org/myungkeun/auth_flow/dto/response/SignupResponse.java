package org.myungkeun.auth_flow.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.myungkeun.auth_flow.entity.Member;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class SignupResponse {
    private Member member;
}
