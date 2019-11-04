package com.sequoiacode.scraper.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderClassName = "Builder", toBuilder = true)
public class AppSecurity implements Serializable {

    private static final long serialVersionUID = 5926468583005150707L;

    private String role;
    private String subject;
    private String mappedUser;
    private String group;
}
