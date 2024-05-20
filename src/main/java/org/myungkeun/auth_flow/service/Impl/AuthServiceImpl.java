package org.myungkeun.auth_flow.service.Impl;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.myungkeun.auth_flow.config.mail.MailService;
import org.myungkeun.auth_flow.config.security.JwtService;
import org.myungkeun.auth_flow.dto.request.*;
import org.myungkeun.auth_flow.dto.response.LoginResponse;
import org.myungkeun.auth_flow.dto.response.SignupResponse;
import org.myungkeun.auth_flow.entity.Member;
import org.myungkeun.auth_flow.entity.Role;
import org.myungkeun.auth_flow.exception.BadRequestException;
import org.myungkeun.auth_flow.exception.NotFoundException;
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
        cacheManager.save(toMail, Integer.toString(authCode), Duration.ofDays(verifiedCodeExpiration));
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
        Object cachedAuthCode = cacheManager.getData(request.getEmail());
        if (cachedAuthCode == null) {
            return false;
        }
        boolean result = cachedAuthCode.equals(request.getAuthNumber());
        if (result) {
            cacheManager.deleteValues(request.getEmail());
            return true;
        } else return false;
    }


    // 1. 사용자가 이메일 입력 -> repository에서 findByEmail 하여 결과값이 있을경우 메일 전송
    // 2. 메일 전송에는 유저가 입력한 email, randomCode, bodyTemplate 을 매개변수로 전송
    // 3. 메일 전송과 동시에 생성한 randomCode를 email을 키값으로 하여 reids에 저장.
    // 4. 유저가 input 에 random 코드를 입력시 redis에 저장된 데이터와 비교후 boolean 리턴
    // 5. 리턴값이 true 일 경우 비밀번호 변경 페이지에 newPassword와 confirmPassword를 입력 두 비밀번호 값이 일치할경우
    // 6. 유저 email을 이용해 repository에 있는 유저 정보를 가져온뒤 password 변경

    @Override
    public String resetPasswordMailSend(ResetPasswordMailSendRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("해당 이메일로 가입된 유저 정보를 찾을 수 없습니다."));
        int randomCode =  makeRandomNumber();
        String setFrom = mailUsername;
        String toMail = member.getEmail();
        String title = "비밀번호 재설정 메일입니다..";
        String content =  "비밀번호 재설정을 위한 인증코드입니다." +
                "<br><br>" +
                "인증 번호는 " + randomCode + "입니다." +
                "<br>" +
                "인증번호를 제대로 입력해주세요";
        cacheManager.save(toMail, Integer.toString(randomCode), Duration.ofDays(300000));
        mailService.mailSend(setFrom, toMail, title, content);
        return "메일이 전송되었습니다.";
    }

    @Override
    public Boolean resetPasswordCheck(ResetPasswordMailCheckRequest request) {
        Object cachedRandomCode = cacheManager.getData(request.getEmail());
        if (cachedRandomCode == null) {
            return false;
        }
        boolean result = cachedRandomCode.equals(request.getRandomCode());
        if (result) {
            cacheManager.deleteValues(request.getEmail());
            return true;
        } else return false;
    }

    @Override
    public Member resetPassword(ResetPasswordRequest request) {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("두 비밀번호가 일치하지 않습니다.");
        }
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("해당 이메일로 가입된 유저 정보를 찾을 수 없습니다."));
        member.setPassword(request.getNewPassword());
        return memberRepository.save(member);
    }
}