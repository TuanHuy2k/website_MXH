package com.hust.webchat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("app.websocket")
public class WebSocketProperties {

    private String applicationPrefix;

    private String topicPrefix;

    private String endpoint;

    private String[] allowedOrigins = {"*"};
}
