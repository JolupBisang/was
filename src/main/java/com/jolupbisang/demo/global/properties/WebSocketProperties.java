package com.jolupbisang.demo.global.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ConfigurationProperties("websocket")
@Component
public class WebSocketProperties {
    private String textBufferSize;
    private String binaryBufferSize;
    private String sessionTimeout;
}
