package com.sequoiacode.api.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JwtToken implements Serializable {
    private static final long serialVersionUID = -8091879091924046844L;
    private String token;
}
