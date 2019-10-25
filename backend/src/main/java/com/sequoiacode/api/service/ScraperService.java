package com.sequoiacode.api.service;

import com.sequoiacode.api.config.ScraperUtil;
import com.sequoiacode.api.config.WASEnv;
import com.sequoiacode.api.domain.Credential;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScraperService {

    public List getApps(Credential credential, WASEnv env){
        return ScraperUtil.getApps(credential,env);
    }

}
