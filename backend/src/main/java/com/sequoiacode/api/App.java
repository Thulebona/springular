package com.sequoiacode.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

@SpringBootApplication
public class App {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(App.class, args);
    }

    @Bean
    protected void startUi() throws IOException {
        String pathToUI = System.getProperty("user.dir").concat("/backend/src/main/resources/static/");
        String active = System.getProperty("spring.profiles.active");
        System.out.println(active);
        System.out.println(pathToUI);
    }


}
