package com.yroots.lambda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.yroots.lambda.configs.FileStorageProperties;

@EnableJpaRepositories
@SpringBootApplication
@EnableConfigurationProperties({
    FileStorageProperties.class
})
public class LambdaApplication {

	public static void main(String[] args) {
		SpringApplication.run(LambdaApplication.class, args);
	}

}
