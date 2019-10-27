package com.sequoiacode.api.repository;

import com.sequoiacode.api.domain.JwtUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtUserRepository extends CrudRepository<JwtUser,String> {
    JwtUser findByUsername(String username);
}
