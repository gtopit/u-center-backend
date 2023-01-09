package com.gtop.uc.common.response;

import com.gtop.uc.common.exception.ApplicationException;
import com.gtop.uc.common.message.MessageHelper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author hongzw
 */
public class ApiResponse<T> implements Response<T> {

    private static final long serialVersionUID = -4323366786484007045L;
    /**
     * 执行成功标识：true-成功；false-失败
     */
    private boolean success;
    /**
     * 提示信息编码
     */
    private int code;
    /**
     * 提示信息内容
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 附加信息：除了data字段的数据外，接口额外返回的附加信息。例如，输入校验失败时，错误字段名和错误内容会在这个节点返回。
     */
    private Map<String, Object> attachments;

    private ApiResponse() {
    }

    public static <T> ApiResponse<T> success() {
        ApiResponse<T> apiResponse = new ApiResponse();
        apiResponse.success = true;
        return apiResponse;
    }

    public static <T> ApiResponse<T> successWithMsg(int code, Object... args) {
        ApiResponse<T> apiResponse = new ApiResponse();
        apiResponse.success = true;
        apiResponse.code = code;
        apiResponse.message = MessageHelper.getApplicationMessage(code, args);
        return apiResponse;
    }

    public static <T> ApiResponse<T> successWithMsgAndData(T data, int code, Object... args) {
        ApiResponse<T> apiResponse = new ApiResponse();
        apiResponse.success = true;
        apiResponse.code = code;
        apiResponse.message = MessageHelper.getApplicationMessage(code, args);
        apiResponse.data = data;
        return apiResponse;
    }

    public static <T> ApiResponse<T> successWithData(T data) {
        ApiResponse<T> apiResponse = new ApiResponse();
        apiResponse.data = data;
        apiResponse.success = true;
        return apiResponse;
    }

    public static <T> ApiResponse<T> failed() {
        ApiResponse<T> apiResponse = new ApiResponse();
        apiResponse.success = false;
        return apiResponse;
    }

    public static <T> ApiResponse<T> failedOf(ApplicationException exception) {
        ApiResponse<T> apiResponse = new ApiResponse();
        apiResponse.success = false;
        apiResponse.code = exception.getErrorCode();
        apiResponse.message = exception.getMessage();
        return apiResponse;
    }

    public static <T> ApiResponse<T> failedWithMsg(int code, Object... args) {
        ApiResponse<T> apiResponse = new ApiResponse();
        apiResponse.success = false;
        apiResponse.code = code;
        apiResponse.message = MessageHelper.getApplicationMessage(code, args);
        return apiResponse;
    }

    @Override
    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setAttachments(Map<String, Object> attachments) {
        this.attachments = (Map)(attachments == null ? new HashMap() : attachments);
    }

    public void setAttachment(String key, Object value) {
        if (this.attachments == null) {
            this.attachments = new HashMap(4);
        }

        this.attachments.put(key, value);
    }

    public void setAttachmentIfAbsent(String key, Object value) {
        if (this.attachments == null) {
            this.attachments = new HashMap(4);
        }

        this.attachments.putIfAbsent(key, value);
    }

    public void addAttachments(Map<String, Object> attachments) {
        if (attachments != null) {
            if (this.attachments == null) {
                this.attachments = new HashMap(4);
            }

            this.attachments.putAll(attachments);
        }
    }

    public void addAttachmentsIfAbsent(Map<String, Object> attachments) {
        if (attachments != null) {
            Iterator var2 = attachments.entrySet().iterator();

            while(var2.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry)var2.next();
                this.setAttachmentIfAbsent((String)entry.getKey(), entry.getValue());
            }

        }
    }

    @Override
    public Object getAttachment(String key) {
        return this.attachments == null ? null : this.attachments.get(key);
    }

    public Object addAttachment(String key, Object value) {
        if (this.attachments == null) {
            this.attachments = new HashMap(4);
        }

        return this.attachments.put(key, value);
    }

    @Override
    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public Map<String, Object> getAttachments() {
        return this.attachments;
    }

}
