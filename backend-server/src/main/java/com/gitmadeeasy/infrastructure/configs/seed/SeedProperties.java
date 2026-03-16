package com.gitmadeeasy.infrastructure.configs.seed;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration @ConfigurationProperties(prefix = "seed")
public class SeedProperties {
    private boolean fromFile;
    private List<String> filePath;

    public boolean isFromFile() { return fromFile; }

    public void setFromFile(boolean fromFile) { this.fromFile = fromFile; }

    public List<String> getFilePath() { return this.filePath; }
    public void setFilePath(List<String> filePath) { this.filePath = filePath; }
}