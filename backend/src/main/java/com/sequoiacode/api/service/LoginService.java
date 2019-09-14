package com.sequoiacode.api.service;

import com.sequoiacode.api.config.ScraperUtil;
import com.sequoiacode.api.config.WASEnv;
import com.sequoiacode.api.domain.Credential;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    public void login(Credential credential, WASEnv env){
        ScraperUtil.getAppAndSecurity(credential,env);
    }
}
