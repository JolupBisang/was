package com.jolupbisang.demo.application.common;

import com.jolupbisang.demo.global.exception.BusinessException;
import com.jolupbisang.demo.global.exception.ErrorCode;

import java.util.List;

public class NotFoundException extends BusinessException {
    public NotFoundException(ErrorCode errorCode, List<Object> values) {
        super(errorCode, values);
    }

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
