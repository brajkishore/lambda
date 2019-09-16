package com.yroots.lambda.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
    private String tmpPath;

	public String getTmpPath() {
		return tmpPath;
	}

	public void setTmpPath(String tmpPath) {
		this.tmpPath = tmpPath;
	}

   
}
