package com.jolupbisang.demo.global.config;

import com.jolupbisang.demo.global.properties.WebSocketProperties;
import com.jolupbisang.demo.presentation.auth.interceptor.WebSocketAuthInterceptor;
import com.jolupbisang.demo.presentation.audio.MeetingSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final WebSocketProperties webSocketProperties;
    private final MeetingSocketHandler meetingSocketHandler;
    private final WebSocketAuthInterceptor webSocketAuthInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(meetingSocketHandler, "/ws/meeting/audio/{meetingId}")
                .addInterceptors(webSocketAuthInterceptor)
                .setAllowedOrigins("*");
    }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(webSocketProperties.getTextBufferSize());
        container.setMaxBinaryMessageBufferSize(webSocketProperties.getBinaryBufferSize());
        container.setMaxSessionIdleTimeout(webSocketProperties.getSessionTimeout());
        return container;
    }
}
