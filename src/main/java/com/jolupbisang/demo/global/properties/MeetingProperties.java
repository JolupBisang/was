package com.jolupbisang.demo.global.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("meeting")
@Getter
@Setter
public class MeetingProperties {
    private String baseDir;
}
