package com.sequoiacode.scraper.service;

import com.sequoiacode.scraper.config.security.JwtTokenUtil;
import com.sequoiacode.scraper.domain.JwtToken;
import com.sequoiacode.scraper.domain.JwtUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private AuthenticationManager authManager;
    private UsernamePasswordAuthenticationToken authToken;

    public JwtToken authenticate(JwtUser jwtUser) {
        authToken = new UsernamePasswordAuthenticationToken(jwtUser.getUsername(), jwtUser.getPassword(), jwtUser.getAuthorities());
        authManager.authenticate(authToken);
        String token = jwtTokenUtil.generateToken(jwtUser);
        return new JwtToken(token);
    }

}
