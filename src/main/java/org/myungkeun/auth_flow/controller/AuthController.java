package org.myungkeun.auth_flow.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.myungkeun.auth_flow.dto.request.LoginRequest;
import org.myungkeun.auth_flow.dto.request.SignupRequest;
import org.myungkeun.auth_flow.dto.request.UpdatePasswordRequest;
import org.myungkeun.auth_flow.dto.response.BaseResponse;
import org.myungkeun.auth_flow.dto.response.LoginResponse;
import org.myungkeun.auth_flow.dto.response.SignupResponse;
import org.myungkeun.auth_flow.entity.Member;
import org.myungkeun.auth_flow.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    ResponseEntity<BaseResponse<LoginResponse>> login(
            @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        LoginResponse result = authService.login(request, response);
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

    @GetMapping("/profile")
    ResponseEntity<BaseResponse<Member>> getMemberInfo(
            Principal connectedUser
    ) {
        Member result = authService.getMemberInfo(connectedUser);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(result, HttpStatus.OK.value()));
    }

    @PutMapping("/update/password")
    ResponseEntity<BaseResponse<Member>> updateMemberPassword(
            Principal connectedUser,
            @RequestBody UpdatePasswordRequest request
    ) {
        Member result = authService.updateMemberPassword(connectedUser, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(result, HttpStatus.OK.value()));
    }

}
