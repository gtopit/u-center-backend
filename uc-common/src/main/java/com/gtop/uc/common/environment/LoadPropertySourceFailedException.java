package com.gtop.uc.common.environment;

/**
 * @author hongzw
 */
public class LoadPropertySourceFailedException extends RuntimeException {

    private static final long serialVersionUID = -3421583357398806165L;

    public LoadPropertySourceFailedException() {
    }

    public LoadPropertySourceFailedException(String message) {
        super(message);
    }

    public LoadPropertySourceFailedException(String message, Throwable cause) {
        super(message, cause);
    }

}
