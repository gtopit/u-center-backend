package com.gtop.uc.common.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author hongzw
 */
public class ValidateCodeException extends AuthenticationException {

    private static final long serialVersionUID = 8369361287664640677L;

    public ValidateCodeException(String msg) {
        super(msg);
    }
}
