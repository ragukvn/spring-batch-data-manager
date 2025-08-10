package com.ragukvn.data.manager.batch.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix = "spring.batch.configs")
@Configuration
public class BatchAppConfigs {

    private String inputFilePath;

    private String jobName;

    private String delimiter;

    private Integer chunkSize;

}
