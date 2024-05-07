package org.myungkeun.auth_flow.service.Impl;

import lombok.RequiredArgsConstructor;
import org.myungkeun.auth_flow.config.security.JwtService;
import org.myungkeun.auth_flow.dto.request.LoginRequest;
import org.myungkeun.auth_flow.dto.request.SignupRequest;
import org.myungkeun.auth_flow.dto.request.UpdatePasswordRequest;
import org.myungkeun.auth_flow.dto.response.LoginResponse;
import org.myungkeun.auth_flow.dto.response.SignupResponse;
import org.myungkeun.auth_flow.entity.Member;
import org.myungkeun.auth_flow.entity.Role;
import org.myungkeun.auth_flow.exception.BadRequestException;
import org.myungkeun.auth_flow.exception.InternalServerErrorException;
import org.myungkeun.auth_flow.repository.MemberRepository;
import org.myungkeun.auth_flow.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor

public class AuthServiceImpl implements AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public LoginResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            Member member = memberRepository.findByEmail(request.getEmail())
                    .orElseThrow();
            String accessToken = jwtService.generateAccessToken(member);
            String refreshToken = jwtService.generateRefreshToken(member);

            LoginResponse response = LoginResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
            return response;
        } catch (Exception e) {
            throw new InternalServerErrorException("서버 오류가 발생했습니다.");
        }
    }

    @Override
    public SignupResponse signup(SignupRequest request) {
        try {
            if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new BadRequestException("이미 가입된 이메일입니다.");            }
            Member member = Member.builder()
                    .email(request.getEmail())
                    .nickname(request.getNickname())
                    .memberName(request.getMemberName())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.ROLE_BASIC)
                    .build();
            Member responseMember =  memberRepository.save(member);

            return SignupResponse.builder()
                    .member(responseMember)
                    .build();
        }  catch (BadRequestException e) {
            throw e; // 이미 발생한 BadRequestException은 그대로 다시 던집니다.
        } catch (Exception e) {
            // 기타 예외가 발생한 경우 서버 오류로 처리합니다.
            throw new InternalServerErrorException("서버 오류가 발생했습니다.");
        }
    }

    @Override
    public Member getMemberInfo(Principal connectedUser) {
        var member = (Member) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        return member;
    }

    @Override
    public Member updateMemberPassword(Principal connectedUser, UpdatePasswordRequest request) {
        Member member = (Member) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        Member oldMember = memberRepository.findByEmail(member.getEmail())
                .orElseThrow();
        oldMember.setPassword(passwordEncoder.encode(request.getNewPassword()));
        Member response =  memberRepository.save(oldMember);
        return response;
    }

}
