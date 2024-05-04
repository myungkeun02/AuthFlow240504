package org.myungkeun.auth_flow.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.myungkeun.auth_flow.Dto.UserCreateRequestDto;
import org.myungkeun.auth_flow.Dto.UserLoginRequestDto;
import org.myungkeun.auth_flow.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")

public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(
            @Valid @RequestBody UserCreateRequestDto request
    ) {
        userService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }
}
