package org.myungkeun.auth_flow.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class UpdatePasswordRequest {
    private String password;
    private String newPassword;
    private String confirmPassword;
}
