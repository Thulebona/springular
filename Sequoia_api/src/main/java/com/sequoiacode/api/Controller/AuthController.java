package com.sequoiacode.api.Controller;


import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api",produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    @GetMapping(value = "index")
    public String index(){
        return "{ \"name\":\"Hello-wold\" }";
    }
}
