package com.sequoiacode.scraper.controller;

import com.sequoiacode.scraper.domain.AppSecurity;
import com.sequoiacode.scraper.domain.WasApp;
import com.sequoiacode.scraper.service.ScrapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "was", produces = MediaType.APPLICATION_JSON_VALUE)
public class ScraperController {

    @Autowired
    private ScrapperService scrapperService;

    @GetMapping(value = "apps")
    public List<WasApp> getAppsOnWas() throws InterruptedException {
        return scrapperService.getAppsInstalled();
    }

    @GetMapping(value = "security")
    public List<AppSecurity> getAppSecurity(@RequestParam String name){
        return scrapperService.getAppSecurity(name);
    }

}
