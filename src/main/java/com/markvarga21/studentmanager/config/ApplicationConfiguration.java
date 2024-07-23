package com.markvarga21.studentmanager.config;

import com.azure.ai.formrecognizer.documentanalysis.DocumentAnalysisClient;
import com.azure.ai.formrecognizer.documentanalysis.DocumentAnalysisClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.markvarga21.studentmanager.util.Generated;
import com.markvarga21.studentmanager.util.LocalDateDeserializer;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Properties;

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
     * The duration for caching the data
     * measured in hours.
     */
    @Value("${cache.duration.hours}")
    private Long cachingDurationInHours;

    /**
     * The host of the mailing.
     */
    @Value("${spring.mail.host}")
    private String mailHost;

    /**
     * The port of the mailing.
     */
    @Value("${spring.mail.port}")
    private Integer mailPort;

    /**
     * The username of the mailing.
     */
    @Value("${spring.mail.username}")
    private String mailUsername;

    /**
     * The password of the mailing.
     */
    @Value("${spring.mail.password}")
    private String mailPassword;

    /**
     * A custom {@code LocalDate} deserializer.
     */
    private final LocalDateDeserializer localDateDeserializer;

    /**
     * The path to the JSON schema.
     */
    private static final String JSON_SCHEMA_PATH = "/schemas/students_draft-07.json";


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

    /**
     * A bean created for caching the data.
     *
     * @param redisConnectionFactory The connection factory for Redis.
     * @return The created bean.
     */
    @Bean
    public CacheManager cacheManager(
            final RedisConnectionFactory redisConnectionFactory
    ) {
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration
                .defaultCacheConfig()
                .entryTtl(Duration.ofHours(1))
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer()));

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(cacheConfiguration)
                .build();
    }

    /**
     * A bean created for sending emails.
     *
     * @return The configured bean.
     */
    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(this.mailHost);
        mailSender.setPort(this.mailPort);
        mailSender.setUsername(this.mailUsername);
        mailSender.setPassword(this.mailPassword);

        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.debug", "true");

        return mailSender;
    }

    /**
     * A bean created for validating JSON data.
     *
     * @return The JSON schema.
     */
    @Bean
    public JsonSchema jsonSchema() {
        return JsonSchemaFactory
                .getInstance(SpecVersion.VersionFlag.V7)
                .getSchema(getClass().getResourceAsStream(JSON_SCHEMA_PATH));
    }
}
