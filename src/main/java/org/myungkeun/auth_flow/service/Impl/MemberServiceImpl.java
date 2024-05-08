package org.myungkeun.auth_flow.service.Impl;

import lombok.RequiredArgsConstructor;
import org.myungkeun.auth_flow.dto.request.UpdatePasswordRequest;
import org.myungkeun.auth_flow.entity.Member;
import org.myungkeun.auth_flow.exception.BadRequestException;
import org.myungkeun.auth_flow.exception.NotFoundException;
import org.myungkeun.auth_flow.repository.MemberRepository;
import org.myungkeun.auth_flow.service.MemberService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor

public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member getMemberInfo(Principal connectedUser) {
        var member = (Member) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        return member;
    }

    @Override
    public Member updateMemberPassword(Principal connectedUser, UpdatePasswordRequest request) {
        Member member = (Member) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new BadRequestException("기존 비밀번호가 올바르지 않습니다.");
        }
        if (request.getNewPassword().matches(request.getConfirmPassword())) {
            throw new BadRequestException("컨펌 비밀번호가 올바르지 않습니다.");
        }
        Member oldMember = memberRepository.findByEmail(member.getEmail())
                .orElseThrow(() -> new NotFoundException("해당 이메일로 등록된 회원을 찾을 수 없습니다."));
        oldMember.setPassword(passwordEncoder.encode(request.getNewPassword()));
        return memberRepository.save(oldMember);
    }

    @Override
    public Boolean existsEmail(String email) {
        return memberRepository.existsByEmail(validateAndReturn(email, "이메일"));
    }

    @Override
    public Boolean existsNickname(String nickname) {
        return memberRepository.existsByNickname(validateAndReturn(nickname, "닉네임"));
    }

    private String validateAndReturn(String value, String field) {
        if (value == null || value.trim().isEmpty()) {
            throw new BadRequestException(field + "이(가) 올바르지 않습니다.");
        }
        return value.trim();
    }
}
