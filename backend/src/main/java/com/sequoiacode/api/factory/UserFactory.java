package com.sequoiacode.api.factory;

import com.sequoiacode.api.domain.JwtUser;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class UserFactory {

    public static UserDetails jwtToUser(JwtUser user){
        return new User(user.getUsername(),user.getPassword(),new ArrayList<>());
    }

    public static  JwtUser userToJwtUser(UserDetails user){
        return JwtUser.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }
}
