package com.sequoiacode.api.controller;

import com.sequoiacode.api.config.WASEnv;
import com.sequoiacode.api.domain.Credential;
import com.sequoiacode.api.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api",produces = MediaType.APPLICATION_JSON_VALUE)
public class CredentialController {

    @Autowired
    private LoginService service;

    @PostMapping("login")
    public String login(@RequestBody Credential credential){
        service.login(credential, WASEnv.WAS_DEV);
        return credential.toString();
    }
}
