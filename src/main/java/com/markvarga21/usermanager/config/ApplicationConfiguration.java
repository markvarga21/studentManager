package com.markvarga21.usermanager.config;

import com.azure.ai.formrecognizer.documentanalysis.DocumentAnalysisClient;
import com.azure.ai.formrecognizer.documentanalysis.DocumentAnalysisClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up the beans.
 */
@Configuration
public class ApplicationConfiguration {
    @Value("${form.recognizer.key}")
    private String formRecognizerKey;
    @Value("${form.recognizer.endpoint}")
    private String formRecognizerEndpoint;
    /**
     * A method used to create a model mapper bean which then can be used anywhere in the application.
     * @return a {@code ModelMapper} instance.
     */
    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }

    @Bean
    public DocumentAnalysisClient getDocumentanalysisClient() {
        return new DocumentAnalysisClientBuilder()
                .credential(new AzureKeyCredential(this.formRecognizerKey))
                .endpoint(this.formRecognizerEndpoint)
                .buildClient();
    }
}
