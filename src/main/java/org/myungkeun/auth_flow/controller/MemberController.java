package org.myungkeun.auth_flow.controller;

import lombok.RequiredArgsConstructor;
import org.myungkeun.auth_flow.dto.request.UpdateInfoRequest;
import org.myungkeun.auth_flow.dto.request.UpdatePasswordRequest;
import org.myungkeun.auth_flow.dto.response.BaseResponse;
import org.myungkeun.auth_flow.entity.Member;
import org.myungkeun.auth_flow.exception.BadRequestException;
import org.myungkeun.auth_flow.exception.NotFoundException;
import org.myungkeun.auth_flow.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/profile")
    ResponseEntity<BaseResponse<Member>> getMemberInfo(
            Principal connectedUser
    ) {
        try {
            Member result = memberService.getMemberInfo(connectedUser);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BaseResponse<>(result, HttpStatus.OK.value(), "member 정보를 가져왔습니다."));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseResponse<>(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

    @PutMapping("/update/password")
    ResponseEntity<BaseResponse<Member>> updateMemberPassword(
            Principal connectedUser,
            @RequestBody UpdatePasswordRequest request
    ) {
        try {
            Member result = memberService.updateMemberPassword(connectedUser, request);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BaseResponse<>(result, HttpStatus.OK.value(), "비밀번호가 수정되었습니다."));
        } catch (BadRequestException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new BaseResponse<>(null, HttpStatus.BAD_REQUEST.value(), e.getMessage()));
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


    @GetMapping("/check-duplicate/email")
    public ResponseEntity<BaseResponse<Boolean>> checkDuplicateEmail(
            @RequestParam String email
    ) {
        try {
            Boolean result = memberService.existsEmail(email);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BaseResponse<>(result, HttpStatus.OK.value(), "이메일 중복을 확인하였습니다."));
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

    @GetMapping("/check-duplicate/nickname")
    public ResponseEntity<BaseResponse<Boolean>> checkDuplicateNickname(
            @RequestParam String nickname
    ) {
        try {
            Boolean result = memberService.existsNickname(nickname);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BaseResponse<>(result, HttpStatus.OK.value(), "비밀번호 중복을 확인하였습니다."));
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

    @PutMapping("/update/info")
    public ResponseEntity<BaseResponse<Member>> updateMemberInfo(
            Principal connectedMember,
            @RequestBody UpdateInfoRequest request
    ) {
        try {
            Member result = memberService.updateMemberInfo(connectedMember, request);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BaseResponse<>(result, HttpStatus.OK.value(), "멤버 정보가 수정되었습니다."));
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