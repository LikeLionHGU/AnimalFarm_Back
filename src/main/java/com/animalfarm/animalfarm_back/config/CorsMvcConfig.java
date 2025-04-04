package com.animalfarm.animalfarm_back.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://lostnfoundservice.netlify.app/", "http://localhost:3000/")
                .allowedMethods("*")
                .allowCredentials(true)
                .allowCredentials(true);
    }
}
