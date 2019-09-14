package com.sequoiacode.api.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderClassName = "Builder",toBuilder = true)
public class Credential {
    private String username;
    private String password;
}
