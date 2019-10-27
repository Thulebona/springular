package com.sequoiacode.api;

import com.sequoiacode.api.domain.JwtUser;
import com.sequoiacode.api.service.JwtUserRequestService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    public CommandLineRunner setupDefaultUser(JwtUserRequestService service) {
        return args -> service.saveOrUpdate(new JwtUser("wasuser","wasuser"));
    }

}
