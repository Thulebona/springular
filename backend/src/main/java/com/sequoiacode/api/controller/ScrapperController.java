package com.sequoiacode.api.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/was", produces = MediaType.APPLICATION_JSON_VALUE)
public class ScrapperController {

    @GetMapping(value = "/apps")
    public ResponseEntity getInstalledApps(){
        return  ResponseEntity.accepted().build();
    }
}
