package org.myungkeun.auth_flow.service;

import jakarta.servlet.http.HttpServletResponse;
import org.myungkeun.auth_flow.dto.request.LoginRequest;
import org.myungkeun.auth_flow.dto.request.SignupRequest;
import org.myungkeun.auth_flow.dto.request.UpdatePasswordRequest;
import org.myungkeun.auth_flow.dto.response.LoginResponse;
import org.myungkeun.auth_flow.dto.response.SignupResponse;
import org.myungkeun.auth_flow.entity.Member;

import java.security.Principal;

public interface AuthService {
    LoginResponse login(LoginRequest request, HttpServletResponse response);

    SignupResponse signup(SignupRequest request);

    Member getMemberInfo(Principal connectedUser);

    Member updateMemberPassword(Principal connectedUser, UpdatePasswordRequest request);

    Boolean existsEmail(String email);
}
