package org.myungkeun.auth_flow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication

public class AuthFlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthFlowApplication.class, args);
    }

}
