package com.gtop.uc.common.response;

import java.io.Serializable;
import java.util.Map;

/**
 * @author hongzw
 */
public interface Response<T> extends Serializable {

    T getData();

    boolean isSuccess();

    String getMessage();

    Object getAttachment(String var1);

    int getCode();

    Map<String, Object> getAttachments();

}
