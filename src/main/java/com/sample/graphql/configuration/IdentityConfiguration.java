package com.sample.graphql.configuration;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "identityservice")
public class IdentityConfiguration {
    private String url;
    private String scope;
    private String clientId;
    private String clientSecret;
    private Integer connectionTimeOut;

}
