package com.cortexia.cortexia_back_end.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // expõe a pasta /uploads para acesso externo
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}

