package com.sequoiacode.api.controller;

import com.sequoiacode.api.config.security.JwtTokenUtil;
import com.sequoiacode.api.domain.JwtToken;
import com.sequoiacode.api.domain.JwtUser;
import com.sequoiacode.api.service.JwtUserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class JwtAuthController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    private UsernamePasswordAuthenticationToken auth;

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody JwtUser jwtUser)  {
        authenticate(jwtUser);
        String token = jwtTokenUtil.generateToken(jwtUser);
        return ResponseEntity.ok(new JwtToken(token));
    }

    private void authenticate(JwtUser jwtUser) {
        auth = new UsernamePasswordAuthenticationToken(jwtUser.getUsername(), jwtUser.getPassword());
        authenticationManager.authenticate(auth);
    }

}
