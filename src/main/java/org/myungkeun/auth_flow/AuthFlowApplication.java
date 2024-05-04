package org.myungkeun.auth_flow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})

public class AuthFlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthFlowApplication.class, args);
    }

}
