package com.qft.web.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "short.message")
public class ShortMessage {
    private String accessKeyId;
    private String secret;
    private String templateCode;
    private String signName;
}
