package com.sequoiacode.api.domain.dao;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CredentialDao {
    private UUID id;
    private String username;
    private String password;
}
