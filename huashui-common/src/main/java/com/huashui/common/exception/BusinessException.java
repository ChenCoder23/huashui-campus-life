package com.huashui.common.exception;

import com.huashui.common.enums.error.ErrorType;
import lombok.Getter;

/**
 * 业务异常
 */
@Getter
public class BusinessException extends RuntimeException {

    private final int code;

    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }


    public BusinessException(ErrorType errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }
}
