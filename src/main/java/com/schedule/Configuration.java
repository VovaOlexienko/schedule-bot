package com.schedule;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Bean(name = "messageSource")
    public MessageSource getMessageResource() {
        ReloadableResourceBundleMessageSource messageResource = new ReloadableResourceBundleMessageSource();
        messageResource.setBasename("classpath:lang/messages");
        messageResource.setDefaultEncoding("UTF-8");
        return messageResource;
    }
}
