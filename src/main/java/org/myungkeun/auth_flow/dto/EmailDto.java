package org.myungkeun.auth_flow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class EmailDto {

    // 이메일 주소
    private String mail;
    // 인증 코드
    private String verifyCode;
}


