package org.myungkeun.auth_flow.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ResetPasswordMailCheckRequest {
    private String email;
    private String randomCode;
}
