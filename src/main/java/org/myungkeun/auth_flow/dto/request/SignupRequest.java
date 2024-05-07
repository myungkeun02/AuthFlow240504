package org.myungkeun.auth_flow.dto.request;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class SignupRequest {
    private String nickname;
    private String email;
    private String memberName;
    private String password;
}
