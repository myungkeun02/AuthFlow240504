package org.myungkeun.auth_flow.service;

import org.myungkeun.auth_flow.dto.request.LoginRequest;
import org.myungkeun.auth_flow.dto.request.SignupRequest;
import org.myungkeun.auth_flow.dto.response.LoginResponse;
import org.myungkeun.auth_flow.dto.response.SignupResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);

    SignupResponse signup(SignupRequest request);
}
