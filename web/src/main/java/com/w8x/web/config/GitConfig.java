package com.w8x.web.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@ToString
@Component
@ConfigurationProperties(prefix = "git")
public class GitConfig {
    private String workspace;
    private String commiter;
    private String mail;
    private String ssh;
    private String message;
}
