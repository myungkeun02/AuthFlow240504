package org.myungkeun.auth_flow.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.myungkeun.auth_flow.exception.NotFoundException;
import org.myungkeun.auth_flow.repository.MemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor

public class ApplicationConfig {
    private final MemberRepository memberRepository;


    //비밀번호를 암호화하여 반환하는 Bean
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new  BCryptPasswordEncoder();
    }

    //사용자 인증정보를 람다식을 통해 데이터베이스에서 가져오는 Bean .. 찾지 못할경우 Exception 반환
    @Bean
    public UserDetailsService userDetailsService() {
        return email ->
                memberRepository.findByEmail(email)
                        .orElseThrow(() -> new NotFoundException("user not found (findByEmail)"));
    }

    //UserDetailsService 와 PasswordEncoder 를 통해 가져온 값을 검증/인증처리하는 Bean
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    //Spring Security 사용자 인증을 관리하고 처리하는 manager Bean
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuditorAware<Long> auditorAware() {
        return null;
    }

}
