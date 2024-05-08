package org.myungkeun.auth_flow.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.myungkeun.auth_flow.dto.request.LoginRequest;
import org.myungkeun.auth_flow.dto.request.SignupRequest;
import org.myungkeun.auth_flow.dto.response.BaseResponse;
import org.myungkeun.auth_flow.dto.response.LoginResponse;
import org.myungkeun.auth_flow.dto.response.SignupResponse;
import org.myungkeun.auth_flow.exception.BadRequestException;
import org.myungkeun.auth_flow.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
        try {
            LoginResponse result = authService.login(request, response);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BaseResponse<>(result, HttpStatus.OK.value(), "로그인 되었습니다."));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseResponse<>(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

    @PostMapping("/signup")
    ResponseEntity<BaseResponse<SignupResponse>> signup(
            @RequestBody SignupRequest request
    ) {
        try {
            SignupResponse result = authService.signup(request);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new BaseResponse<>(result, HttpStatus.CREATED.value(),"회원가입 되었습니다."));
        } catch (BadRequestException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new BaseResponse<>(null, HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseResponse<>(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

}
