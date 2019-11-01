package com.sequoiacode.scraper.config.security;

import com.sequoiacode.scraper.config.ScraperUtil;
import com.sequoiacode.scraper.domain.JwtUser;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;


@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {


    @Autowired
    private ScraperUtil scraperUtil;

    public CustomAuthenticationProvider() {
        super();
    }

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        final String name = authentication.getName();
        final String password = authentication.getCredentials().toString();
        final JwtUser user = new JwtUser(name, password);
        final boolean isAuth = authThirdParty(user);
        return isAuth ? new UsernamePasswordAuthenticationToken(name, password, user.getAuthorities()) : null;

    }

    private boolean authThirdParty(JwtUser jwtUser) throws BadCredentialsException {
        return scraperUtil.login(jwtUser).findElements(By.tagName("frame")).size() > 0;
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}