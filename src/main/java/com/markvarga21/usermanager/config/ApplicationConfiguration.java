package com.markvarga21.usermanager.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up the beans.
 */
@Configuration
public class ApplicationConfiguration {
    /**
     * A method used to create a model mapper bean which then can be used anywhere in the application.
     * @return a {@code ModelMapper} instance.
     */
    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }
}
