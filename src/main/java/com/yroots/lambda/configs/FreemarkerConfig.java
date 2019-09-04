package com.yroots.lambda.configs;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

@Configuration
public class FreemarkerConfig {

	@Value("${template.path}")
	private String path;
	
	@Primary
    @Bean
    public FreeMarkerConfigurationFactoryBean getFreeMarkerConfiguration() throws IOException {
        FreeMarkerConfigurationFactoryBean bean = new FreeMarkerConfigurationFactoryBean();        
        bean.setTemplateLoaderPath(path);
        return bean;
    }
}
