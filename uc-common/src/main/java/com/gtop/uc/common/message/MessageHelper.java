package com.gtop.uc.common.message;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Map;

/**
 * @author hongzw
 */
@Component
public class MessageHelper {
    private static MessageHelper messageHelper;
    private final MessageProperties messageProperties;
    @Resource
    private ApplicationContext applicationContext;

    @PostConstruct
    public void messageHelper() {
        messageHelper = applicationContext.getBean(MessageHelper.class);
    }

    public MessageHelper(MessageProperties messageProperties) {
        messageProperties.validate();
        this.messageProperties = messageProperties;
    }
    private String findSystemMessage(int code, Object... args) {
        Map<Integer, String> systemMessageMap = this.messageProperties.getSystemMessageMap();
        if (systemMessageMap != null && systemMessageMap.size() != 0 && systemMessageMap.containsKey(code)) {
            String message = systemMessageMap.get(code);
            return MessageFormat.format(message, args);
        } else {
            throw new IllegalArgumentException("找不到Code:" + code + "对应的提示信息。请检查[gtop.messages.system-message-map]配置。");
        }
    }

    private String findApplicationMessage(int code, Object... args) {
        Map<Integer, String> applicationMessageMap = this.messageProperties.getApplicationMessageMap();
        if (applicationMessageMap != null && applicationMessageMap.size() != 0 && applicationMessageMap.containsKey(code)) {
            String message = applicationMessageMap.get(code);
            return MessageFormat.format(message, args);
        } else {
            throw new IllegalArgumentException("找不到Code:" + code + "对应的提示信息。请检查[gtop.messages.application-message-map]配置。");
        }
    }

    private String findValidatorMessage(int code, Object... args) {
        Map<Integer, String> validatorMessageMap = this.messageProperties.getValidatorMessageMap();
        if (validatorMessageMap != null && validatorMessageMap.size() != 0 && validatorMessageMap.containsKey(code)) {
            String message = validatorMessageMap.get(code);
            return MessageFormat.format(message, args);
        } else {
            throw new IllegalArgumentException("找不到Code:" + code + "对应的提示信息。请检查[gtop.messages.validator-message-map]配置。");
        }
    }

    public static String getSystemMessage(int code, Object... args) {
        return messageHelper.findSystemMessage(code, args);
    }

    public static String getApplicationMessage(int code, Object... args) {
        return code <= 999 ? messageHelper.findSystemMessage(code, args) : messageHelper.findApplicationMessage(code, args);
    }

    public static String getValidatorMessage(int code, Object... args) {
        return messageHelper.findValidatorMessage(code, args);
    }
}
