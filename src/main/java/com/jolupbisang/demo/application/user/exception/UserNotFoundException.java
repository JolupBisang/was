package com.jolupbisang.demo.application.user.exception;

import com.jolupbisang.demo.application.common.NotFoundException;

import java.util.List;

public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException(List<Object> values) {
        super(UserApplicationErrorCode.NOT_FOUND_USER, values);
    }

    public UserNotFoundException() {
        super(UserApplicationErrorCode.NOT_FOUND_USER);
    }
}
