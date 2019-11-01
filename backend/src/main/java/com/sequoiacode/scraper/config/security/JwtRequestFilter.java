package com.sequoiacode.scraper.config.security;

import com.sequoiacode.scraper.domain.JwtUser;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    private static final Logger LOGGER = LogManager.getLogger(JwtRequestFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String username, jwtToken = username = null;
        final String requestTokenHeader = request.getHeader("Authorization");
        final Authentication authContext = SecurityContextHolder.getContext().getAuthentication();

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                LOGGER.debug("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                LOGGER.debug("JWT Token has expired");
            }
        } else {
            LOGGER.warn("JWT Token does not begin with Bearer String");
        }

        if (username != null && authContext == null) {
            JwtUser jwtUser = new JwtUser(username,"");
            if (jwtTokenUtil.validateToken(jwtToken, jwtUser)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(jwtUser, null, jwtUser.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        chain.doFilter(request, response);
    }
}
