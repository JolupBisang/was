package com.jolupbisang.demo.global.config;

import com.jolupbisang.demo.global.properties.WebSocketProperties;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.WebSocketContainer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

@Configuration
@RequiredArgsConstructor
public class WhisperWebSocketConfig {

    private final WebSocketProperties webSocketProperties;

    @Bean
    public WebSocketClient whisperWebSocketClient() {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        container.setDefaultMaxTextMessageBufferSize(webSocketProperties.getTextBufferSize());
        container.setDefaultMaxBinaryMessageBufferSize(webSocketProperties.getBinaryBufferSize());
        container.setDefaultMaxSessionIdleTimeout(webSocketProperties.getSessionTimeout());

        return new StandardWebSocketClient(container);
    }
}
