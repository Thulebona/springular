package com.sequoiacode.scraper.controller;

import com.sequoiacode.scraper.domain.JwtToken;
import com.sequoiacode.scraper.domain.JwtUser;
import com.sequoiacode.scraper.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    @Autowired
    private AuthenticationService authService;

    @PostMapping(value = "login")
    public ResponseEntity<?> login(@RequestBody JwtUser jwtUser, @RequestHeader(value = "User-Agent") String userAgent) {
        JwtToken token = authService.authenticate(jwtUser, userAgent);
        return ResponseEntity.ok(token);
    }

}
