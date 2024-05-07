package org.myungkeun.auth_flow.controller;

import lombok.RequiredArgsConstructor;
import org.myungkeun.auth_flow.dto.request.LoginRequest;
import org.myungkeun.auth_flow.dto.request.SignupRequest;
import org.myungkeun.auth_flow.dto.response.BaseResponse;
import org.myungkeun.auth_flow.dto.response.LoginResponse;
import org.myungkeun.auth_flow.dto.response.SignupResponse;
import org.myungkeun.auth_flow.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    ResponseEntity<BaseResponse<LoginResponse>> login(
            @RequestBody LoginRequest request
    ) {
        LoginResponse result = authService.login(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(result, HttpStatus.OK.value()));
    }

    @PostMapping("/signup")
    ResponseEntity<BaseResponse<SignupResponse>> signup(
            @RequestBody SignupRequest request
    ) {
        SignupResponse result = authService.signup(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new BaseResponse<>(result, HttpStatus.CREATED.value()));
    }
}
