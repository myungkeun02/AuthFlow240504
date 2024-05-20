package org.myungkeun.auth_flow.controller;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.myungkeun.auth_flow.dto.request.*;
import org.myungkeun.auth_flow.dto.response.BaseResponse;
import org.myungkeun.auth_flow.dto.response.LoginResponse;
import org.myungkeun.auth_flow.dto.response.SignupResponse;
import org.myungkeun.auth_flow.entity.Member;
import org.myungkeun.auth_flow.exception.BadRequestException;
import org.myungkeun.auth_flow.exception.NotFoundException;
import org.myungkeun.auth_flow.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;


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
                    .body(new BaseResponse<>(result, HttpStatus.CREATED.value(), "회원가입 되었습니다."));
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

    @PostMapping("/mail/send")
    ResponseEntity<BaseResponse<String>> authMailSend(
            @RequestBody MailRequest mailRequest
    ) {
        try {
            String result = authService.joinEmail(mailRequest);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BaseResponse<>(result, HttpStatus.OK.value(), "메일이 전송 되었습니다."));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseResponse<>(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

    @PostMapping("/mail/check")
    ResponseEntity<BaseResponse<Boolean>> checkMail(
            @RequestBody MailCheckRequest request
    ) {
        try {
            Boolean result = authService.checkAuthNumber(request);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BaseResponse<>(result, HttpStatus.OK.value(), "인증결과"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseResponse<>(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));

        }
    }

    @PostMapping("/reset-password/send")
    ResponseEntity<BaseResponse<String>> resetPasswordMailSend(
            @RequestBody ResetPasswordMailSendRequest request
    ) {
        try {
            String result = authService.resetPasswordMailSend(request);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BaseResponse<>(result, HttpStatus.OK.value(), "메일이 전송되었습니다."));
        } catch (NotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new BaseResponse<>(null, HttpStatus.NOT_FOUND.value(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseResponse<>(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

    @PostMapping("/reset-password/check")
    ResponseEntity<BaseResponse<Boolean>> resetPasswordMailCheck(
            @RequestBody ResetPasswordMailCheckRequest request
    ) {
        try {
            Boolean result = authService.resetPasswordCheck(request);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BaseResponse<>(result, HttpStatus.OK.value(), "메일인증결과"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseResponse<>(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

    @PutMapping("/reset-password")
    ResponseEntity<BaseResponse<Member>> resetPassword(
            @RequestBody ResetPasswordRequest request
    ) {
        try {
            Member result = authService.resetPassword(request);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BaseResponse<>(result, HttpStatus.OK.value(), "비밀번호가 변경되었습니다."));
        } catch (NotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new BaseResponse<>(null, HttpStatus.NOT_FOUND.value(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseResponse<>(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }
}

