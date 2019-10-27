package com.sequoiacode.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sequoiacode.api.config.ErrorHandler;
import com.sequoiacode.api.config.security.JwtTokenUtil;
import com.sequoiacode.api.domain.JwtToken;
import com.sequoiacode.api.domain.JwtUser;
import com.sequoiacode.api.service.JwtUserRequestService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
@RequestMapping(value = "/api/auth",produces = MediaType.APPLICATION_JSON_VALUE)
public class JwtAuthController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private JwtUserRequestService service;
    @Autowired
    private AuthenticationManager authenticationManager;
    private static final Logger LOGGER = LogManager.getLogger(JwtAuthController.class);

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody JwtUser jwtUser) throws Exception {
        authenticate(jwtUser);
        String token = jwtTokenUtil.generateToken(jwtUser);
        return ResponseEntity.ok(new JwtToken(token));
    }

    private void authenticate(JwtUser jwtUser ) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtUser.getUsername(), jwtUser.getPassword()));
        } catch (Exception e) {
            ErrorHandler.handleException(e, LOGGER);
        }
    }

}
