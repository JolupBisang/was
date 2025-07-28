package com.jolupbisang.demo.global.exception;

import java.util.Map;

public class BusinessException extends ServiceLogicException {

    public BusinessException(ErrorCode errorCode, Map<String, Object> values) {
        super(errorCode, values);
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode);
    }
}
