package com.sherlocky.springboot2.shirojwt.exception;

/**
 * 公用的Exception
 * <p>
 * 继承自RuntimeException
 * </p>
 */
public class BaseException extends RuntimeException {
    public BaseException() {
        super();
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
