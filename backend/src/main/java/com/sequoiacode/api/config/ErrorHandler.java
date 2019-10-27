package com.sequoiacode.api.config;

import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;

public class ErrorHandler {

    public static  <T extends Exception> void handleException(T e, Logger LOGGER ) throws Exception {
        LOGGER.error(e,e);
        if (e instanceof DisabledException){
            throw new Exception("USER_DISABLED", e);
        }else if (e instanceof  BadCredentialsException){
            throw new Exception("USER_DISABLED", e);
        }
    }
}
