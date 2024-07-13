package com.markvarga21.studentmanager.config;

import com.azure.ai.formrecognizer.documentanalysis.DocumentAnalysisClient;
import com.azure.ai.formrecognizer.documentanalysis.DocumentAnalysisClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.markvarga21.studentmanager.util.Generated;
import com.markvarga21.studentmanager.util.LocalDateDeserializer;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

/**
 * Configuration class for setting up the beans.
 */
@Configuration
@RequiredArgsConstructor
@OpenAPIDefinition(
        info = @io.swagger.v3.oas.annotations.info.Info(
                title = "Student Manager API",
                version = "1.0",
                description = "API for managing students."
        )
)
@Generated
public class ApplicationConfiguration {
    /**
     * The header name used by Azure's Face API
     * for using its services.
     */
    public static final String AZURE_API_KEY_HEADER
            = "Ocp-Apim-Subscription-Key";

    /**
     * The Azure's Form Recognizer service key.
     */
    @Value("${knopp.services.key}")
    private String formRecognizerKey;

    /**
     * The Face API URL and endpoint.
     */
    @Value("${knopp.services.endpoint}")
    private String formRecognizerEndpoint;

    /**
     * A custom {@code LocalDate} deserializer.
     */
    private final LocalDateDeserializer localDateDeserializer;


    /**
     * A bean for creating a client for the Azure's Form Recognizer service.
     *
     * @return The built {@code DocumentAnalysisClient} object.
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
     * @return The built {@code Gson} object.
     */
    @Bean
    public Gson getGsonLocalDateDeserializer() {
        return new GsonBuilder()
                .registerTypeAdapter(
                        LocalDate.class,
                        this.localDateDeserializer
                )
                .create();
    }

    /**
     * A bean created for talking with the APIs like
     * Azure's Face API and Rest Countries API.
     *
     * @return The created bean.
     */
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
