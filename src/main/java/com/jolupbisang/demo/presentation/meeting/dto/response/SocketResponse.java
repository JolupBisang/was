package com.jolupbisang.demo.presentation.meeting.dto.response;

import com.jolupbisang.demo.global.response.ErrorResponse;
import lombok.Getter;

@Getter
public class SocketResponse<T> {
    private final SocketResponseType type;
    private final T data;

    private SocketResponse(SocketResponseType type, T data) {
        this.type = type;
        this.data = data;
    }

    public static <T> SocketResponse<T> of(SocketResponseType type, T data) {
        return new SocketResponse<>(type, data);
    }

    public static SocketResponse<Void> of(SocketResponseType type) {
        return new SocketResponse<>(type, null);
    }
    
    public static SocketResponse<ErrorResponse> error(String errorMessage, String errorId) { 
        ErrorResponse errorData = ErrorResponse.of(errorMessage, errorId);
        return new SocketResponse<>(SocketResponseType.ERROR, errorData);
    }
} 