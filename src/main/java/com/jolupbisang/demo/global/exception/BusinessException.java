package com.jolupbisang.demo.global.exception;

import java.util.List;

public class BusinessException extends ServiceLogicException {

    public BusinessException(ErrorCode errorCode, List<Object> values) {
        super(errorCode, values);
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode);
    }
}
