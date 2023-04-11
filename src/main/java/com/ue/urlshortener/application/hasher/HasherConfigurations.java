package com.ue.urlshortener.application.hasher;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "hasher")
@Configuration
@Data
public class HasherConfigurations {
    private int length;
    private String pool; // browser/client support to chars in url generated needs to be verified
}
