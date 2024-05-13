package org.myungkeun.auth_flow.service;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import org.myungkeun.auth_flow.dto.request.LoginRequest;
import org.myungkeun.auth_flow.dto.request.SignupRequest;
import org.myungkeun.auth_flow.dto.response.LoginResponse;
import org.myungkeun.auth_flow.dto.response.SignupResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request, HttpServletResponse response);

    SignupResponse signup(SignupRequest request) throws MessagingException;
}
