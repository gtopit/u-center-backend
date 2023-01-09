package com.gtop.uc.common.exception;

import cn.hutool.core.date.DateUtil;
import com.gtop.uc.common.exception.ApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;

import java.nio.charset.StandardCharsets;

/**
 * @author hongzw
 */
@Slf4j
public class Auth2ResponseExceptionTranslator implements WebResponseExceptionTranslator {

    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) {
        log.error("Auth2异常", e);
        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8);
        headers.setContentType(mediaType);
        if (e instanceof OAuth2Exception) {
            return new ResponseEntity(getErrorMessageString(HttpStatus.BAD_REQUEST.value(), e.getMessage()), headers, HttpStatus.UNAUTHORIZED);
        }
        if (e instanceof InternalAuthenticationServiceException) {
            Throwable throwable = e.getCause();
            if (throwable instanceof ApplicationException) {
                ApplicationException exception = (ApplicationException) throwable;
                int code = exception.getErrorCode();
                return new ResponseEntity(getErrorMessageString(code, exception.getMessage()), headers, HttpStatus.UNAUTHORIZED);
            }
        }
        return new ResponseEntity(getErrorMessageString(HttpStatus.METHOD_NOT_ALLOWED.value(), e.getMessage()), headers, HttpStatus.METHOD_NOT_ALLOWED);
    }

    private String getErrorMessageString(Integer code, String message) {
        return "{\"success\":false,\"code\":" + code + ",\"lastTime\":\"" + DateUtil.now() + "\",\"message\":\"" + message.replace("'", "\\'") + "\"}";
    }

}
