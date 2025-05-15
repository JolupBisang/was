package com.jolupbisang.demo.global.response;

public record SocketResponse<T>(
        SocketResponseType type,
        T data
) {
    public static <T> SocketResponse<T> of(SocketResponseType type, T data) {
        return new SocketResponse<>(type, data);
    }
} 