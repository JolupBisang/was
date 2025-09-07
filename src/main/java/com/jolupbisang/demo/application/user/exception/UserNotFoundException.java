package com.jolupbisang.demo.application.user.exception;

import com.jolupbisang.demo.global.exception.NotFoundException;

import java.util.Map;

public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException(Map<String, Object> values) {
        super(UserApplicationErrorCode.NOT_FOUND_USER, values);
    }

    public UserNotFoundException() {
        super(UserApplicationErrorCode.NOT_FOUND_USER);
    }
}
