package com.gtop.uc.common.message;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * @author hongzw
 */
@Configuration
@ConfigurationProperties(
        prefix = "gtop.messages"
)
public class MessageProperties {
    private Map<Integer, String> systemMessageMap;
    private Map<Integer, String> applicationMessageMap;
    private Map<Integer, String> validatorMessageMap;

    public void validate() {
        if (!CollectionUtils.isEmpty(this.applicationMessageMap)) {
            this.applicationMessageMap.keySet().forEach((code) -> {
                if (code <= 999) {
                    throw new IllegalArgumentException("提示信息Code值错误，小于1000由基础框架保留使用，请检查ccuap.messages.application-message-map的配置。Code:" + code);
                }
            });
        }

        if (!CollectionUtils.isEmpty(this.validatorMessageMap)) {
            this.validatorMessageMap.keySet().forEach((code) -> {
                if (code <= 999) {
                    throw new IllegalArgumentException("提示信息Code值错误，小于1000由基础框架保留使用，请检查ccuap.messages.validator-message-map的配置。");
                }
            });
        }

    }

    public MessageProperties() {
    }
    public Map<Integer, String> getSystemMessageMap() {
        return this.systemMessageMap;
    }

    public Map<Integer, String> getApplicationMessageMap() {
        return this.applicationMessageMap;
    }

    public Map<Integer, String> getValidatorMessageMap() {
        return this.validatorMessageMap;
    }

    public void setSystemMessageMap(Map<Integer, String> systemMessageMap) {
        this.systemMessageMap = systemMessageMap;
    }

    public void setApplicationMessageMap(Map<Integer, String> applicationMessageMap) {
        this.applicationMessageMap = applicationMessageMap;
    }

    public void setValidatorMessageMap(Map<Integer, String> validatorMessageMap) {
        this.validatorMessageMap = validatorMessageMap;
    }

    @Override
    public String toString() {
        return "MessageProperties(systemMessageMap=" + this.getSystemMessageMap() + ", applicationMessageMap=" + this.getApplicationMessageMap() + ", validatorMessageMap=" + this.getValidatorMessageMap() + ")";
    }

}
