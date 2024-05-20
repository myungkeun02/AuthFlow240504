package org.myungkeun.auth_flow.service;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import org.myungkeun.auth_flow.dto.request.*;
import org.myungkeun.auth_flow.dto.response.LoginResponse;
import org.myungkeun.auth_flow.dto.response.SignupResponse;
import org.myungkeun.auth_flow.entity.Member;

import java.security.NoSuchAlgorithmException;

public interface AuthService {
    LoginResponse login(LoginRequest request, HttpServletResponse response);

    SignupResponse signup(SignupRequest request) throws MessagingException;

    String joinEmail(MailRequest request);

    Boolean checkAuthNumber(MailCheckRequest request);

    String resetPasswordMailSend(ResetPasswordMailSendRequest request);

    Boolean resetPasswordCheck(ResetPasswordMailCheckRequest request);

    Member resetPassword(ResetPasswordRequest request);
}
