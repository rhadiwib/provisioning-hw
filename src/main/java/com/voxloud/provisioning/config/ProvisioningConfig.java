package com.voxloud.provisioning.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
import java.util.List;
import java.util.Arrays;

@Configuration
@ConfigurationProperties(prefix = "provisioning")
@Data
public class ProvisioningConfig {
    private String domain;
    private String port;
    private String codecs;

    public List<String> getCodecsList() {
        return Arrays.asList(codecs.split(","));
    }
}