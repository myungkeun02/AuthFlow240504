package org.myungkeun.auth_flow.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.myungkeun.auth_flow.Dto.UserLoginRequestDto;
import org.myungkeun.auth_flow.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")

public class AuthController {
//    private final AuthService authService;

//    @PostMapping("/login")
//    public ResponseEntity<String> login(
//            @Valid @RequestBody UserLoginRequestDto request
//    ) {
//        return;
//    }
}
