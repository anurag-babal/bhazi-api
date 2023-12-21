package com.example.bhazi.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class ApplicationConfiguration {
    @Bean
    ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.addBasenames("messages/exception_message");
        source.setUseCodeAsDefaultMessage(true);
        return source;
    }
}
