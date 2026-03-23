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
    private Integer textBufferSize;
    private Integer binaryBufferSize;
    private Long sessionTimeout;
}
