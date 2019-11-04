package com.sequoiacode.scraper.service;

import com.sequoiacode.scraper.config.ScraperUtil;
import com.sequoiacode.scraper.domain.AppSecurity;
import com.sequoiacode.scraper.domain.WasApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScrapperService {

    @Autowired
    private ScraperUtil scraperUtil;

    public List<WasApp> getAppsInstalled() {
        return scraperUtil.getAppsInstalled();
    }

    public List<AppSecurity> getAppSecurity(String name) {
        return scraperUtil.getAppSecurity(name);
    }
}
