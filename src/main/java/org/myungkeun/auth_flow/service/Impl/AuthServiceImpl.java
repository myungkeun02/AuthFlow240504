package org.myungkeun.auth_flow.service.Impl;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.myungkeun.auth_flow.config.security.JwtService;
import org.myungkeun.auth_flow.dto.request.LoginRequest;
import org.myungkeun.auth_flow.dto.request.SignupRequest;
import org.myungkeun.auth_flow.dto.response.LoginResponse;
import org.myungkeun.auth_flow.dto.response.SignupResponse;
import org.myungkeun.auth_flow.entity.Member;
import org.myungkeun.auth_flow.entity.Role;
import org.myungkeun.auth_flow.exception.BadRequestException;
import org.myungkeun.auth_flow.exception.InternalServerErrorException;
import org.myungkeun.auth_flow.repository.MemberRepository;
import org.myungkeun.auth_flow.service.AuthService;
import org.myungkeun.auth_flow.util.CacheManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor

public class AuthServiceImpl implements AuthService {
    @Value("${application.security.jwt.access-token.expiration}")
    private long accessExpiration;

    private final CacheManager cacheManager;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public LoginResponse login(LoginRequest request, HttpServletResponse response) {
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

            cacheManager.save(member.getEmail(), refreshToken, Duration.ofMinutes(604800000));

            ResponseCookie cookie = ResponseCookie.from("accessToken", accessToken)
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(accessExpiration)
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            LoginResponse result = LoginResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
            return result;
    }

    @Override
    public SignupResponse signup(SignupRequest request) {
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
    }
}
