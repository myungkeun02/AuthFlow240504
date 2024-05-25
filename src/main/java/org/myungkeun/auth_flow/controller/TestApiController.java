package org.myungkeun.auth_flow.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")

public class TestApiController {
    @GetMapping("/hello")
    public ResponseEntity<Object> testApi() {
        String result = "Api 통신에 성공하였습니다.";
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
