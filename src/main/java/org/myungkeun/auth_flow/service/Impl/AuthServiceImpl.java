package org.myungkeun.auth_flow.service.Impl;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.myungkeun.auth_flow.config.mail.MailService;
import org.myungkeun.auth_flow.config.security.JwtService;
import org.myungkeun.auth_flow.dto.request.LoginRequest;
import org.myungkeun.auth_flow.dto.request.MailCheckRequest;
import org.myungkeun.auth_flow.dto.request.MailRequest;
import org.myungkeun.auth_flow.dto.request.SignupRequest;
import org.myungkeun.auth_flow.dto.response.LoginResponse;
import org.myungkeun.auth_flow.dto.response.SignupResponse;
import org.myungkeun.auth_flow.entity.Member;
import org.myungkeun.auth_flow.entity.Role;
import org.myungkeun.auth_flow.exception.BadRequestException;
import org.myungkeun.auth_flow.repository.MemberRepository;
import org.myungkeun.auth_flow.service.AuthService;
import org.myungkeun.auth_flow.util.CacheManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.Random;


@Service
@RequiredArgsConstructor

public class AuthServiceImpl implements AuthService {
    @Value("${application.security.jwt.access-token.expiration}")
    private long accessExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;
    @Value("${application.mail.verified-code.expiration}")
    private long verifiedCodeExpiration;
    @Value("${spring.mail.username}")
    private String mailUsername;

    private static final String AUTH_CODE_PREFIX = "AuthCode ";

    private final CacheManager cacheManager;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final MailService mailService;

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
        cacheManager.save(member.getEmail(), refreshToken, Duration.ofMinutes(refreshExpiration));
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
        checkDuplicatedEmail(request.getEmail());
        Member member = Member.builder()
                .email(request.getEmail())
                .nickname(request.getNickname())
                .memberName(request.getMemberName())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(false)
                .role(Role.ROLE_BASIC)
                .build();
        Member responseMember = memberRepository.save(member);
        return SignupResponse.builder()
                .member(responseMember)
                .build();
    }

    @Override
    public String joinEmail(MailRequest request) {
        int authCode = makeRandomNumber();
        String setFrom = mailUsername;
        String toMail = request.getEmail();
        String title = "회원가입인증 이메일입니다.";
        String content =  "나의 APP을 방문해주셔서 감사합니다." +
                "<br><br>" +
                "인증 번호는 " + authCode + "입니다." +
                "<br>" +
                "인증번호를 제대로 입력해주세요";
        mailService.mailSend(setFrom, toMail, title, content);
        cacheManager.save(Integer.toString(authCode), toMail, Duration.ofDays(verifiedCodeExpiration));
        return Integer.toString(authCode);
    }

    private int makeRandomNumber() {
        Random random = new Random();
        String randomNUmber = "";
        for (int i =0; i < 6; i++) {
            randomNUmber += Integer.toString(random.nextInt(10));
        }
        return Integer.parseInt(randomNUmber);
    }

    private void checkDuplicatedEmail(String email) {
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new BadRequestException("이미 가입된 이메일 입니다.");
        }
    }

    @Override
    public Boolean checkAuthNumber(MailCheckRequest request) {
        String cachedEmail = cacheManager.getData(request.getAuthNumber());
        if (cachedEmail == null) {
            return false;
        }
        return cachedEmail.equals(request.getAuthNumber());
    }

}