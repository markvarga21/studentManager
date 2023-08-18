package com.markvarga21.usermanager.config;

import com.azure.ai.formrecognizer.documentanalysis.DocumentAnalysisClient;
import com.azure.ai.formrecognizer.documentanalysis.DocumentAnalysisClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.markvarga21.usermanager.util.mapping.LocalDateDeserializer;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

/**
 * Configuration class for setting up the beans.
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfiguration {
    /**
     * The Azure's Form Recognizers services key.
     */
    @Value("${form.recognizer.key}")
    private String formRecognizerKey;
    @Value("${form.recognizer.endpoint}")
    private String formRecognizerEndpoint;
    private final LocalDateDeserializer localDateDeserializer;
    /**
     * A method used to create a model mapper bean which then can be used anywhere in the application.
     * @return a {@code ModelMapper} instance.
     */
    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }

    /**
     * A bean for setting up the ID document analysis.
     *
     * @return the already sat up {@code DocumentAnalysisClient}.
     */
    @Bean
    public DocumentAnalysisClient getDocumentanalysisClient() {
        return new DocumentAnalysisClientBuilder()
                .credential(new AzureKeyCredential(this.formRecognizerKey))
                .endpoint(this.formRecognizerEndpoint)
                .buildClient();
    }

    /**
     * A bean created for deserializing user JSON strings into POJO.
     *
     * @return the built {@code Gson} object.
     */
    @Bean
    public Gson getGsonDeserializer() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, this.localDateDeserializer)
                .create();
    }
}
