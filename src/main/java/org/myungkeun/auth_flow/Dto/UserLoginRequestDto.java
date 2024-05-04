package org.myungkeun.auth_flow.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UserLoginRequestDto {
    @NotBlank(message = "email-id must not be empty")
    @Email(message = "email-id must be of valid format")
    private String emailId;

    @NotBlank(message = "password must not be empty")
    private String password;
}
