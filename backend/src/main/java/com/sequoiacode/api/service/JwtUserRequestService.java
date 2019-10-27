package com.sequoiacode.api.service;

import com.sequoiacode.api.domain.JwtUser;
import com.sequoiacode.api.repository.JwtUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service("JwtUserRequestService")
public class JwtUserRequestService implements UserDetailsService {

    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private JwtUserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        JwtUser user = repository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("Username[%s] not found",username));
        }
        return user;
    }

    public JwtUser saveOrUpdate(JwtUser jwtUser) {
        JwtUser user = repository.findByUsername(jwtUser.getUsername());
        if (!(user != null && !encoder.matches(jwtUser.getPassword(), user.getPassword()))) {
            jwtUser = jwtUser.toBuilder().password(encoder.encode(jwtUser.getPassword())).build();
        }
        return repository.save(jwtUser);
    }
}
