package com.ue.urlshortener.application.hasher;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "database")
@Configuration
@Data
public class HasherConfigurations {
    private int length;
    private String pool;
}
