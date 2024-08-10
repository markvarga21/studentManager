package com.markvarga21.studentmanager.config.security;

import com.markvarga21.studentmanager.exception.handler.security.AppAccessDeniedHandler;
import com.markvarga21.studentmanager.service.auth.security.AppUserDetailsService;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Configuration class for setting up the security related
 * configurations.
 */
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
    /**
     * The base URL for authentication services.
     */
    static final String BASE_AUTH_URL = "/api/v1/auth";

    /**
     * The {@code AppUserDetailsService} object.
     */
    private final AppUserDetailsService appUserDetailsService;

    /**
     * The {@code AppAccessDeniedHandler} object.
     */
    private final AppAccessDeniedHandler appAccessDeniedHandler;

    /**
     * The entrypoint of the application.
     */
    private final AppAuthenticationEntryPoint entryPoint;

    /**
     * The application context.
     */
    private final ApplicationContext applicationContext;

    /**
     * The URL for the frontend.
     */
    @Value("${frontend.url}")
    private String frontendUrl;

    /**
     * This method is used to configure the CORS configuration source.
     *
     * @return The built {@code CorsConfigurationSource} object.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of(this.frontendUrl));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Access-Control-Allow-Origin"));
        corsConfiguration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source
                .registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    /**
     * This method is used to configure the security filter chain.
     *
     * @param httpSecurity The {@code HttpSecurity} object to configure the security filter chain.
     * @return The {@code SecurityFilterChain} object.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity httpSecurity) throws Exception {
        JwtAuthFilter jwtAuthFilter = this.applicationContext
                .getBean(JwtAuthFilter.class);

        String[] permittedEndpoints = {
                BASE_AUTH_URL + "/register",
                BASE_AUTH_URL + "/login",
                BASE_AUTH_URL + "/logout",
                "/**"
        };
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(registry -> registry
                    .requestMatchers(permittedEndpoints).permitAll()
                    .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                    .authenticationEntryPoint(this.entryPoint)
                    .accessDeniedHandler(this.appAccessDeniedHandler))
                .sessionManagement(sessionManagement -> sessionManagement
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(
                        jwtAuthFilter,
                        UsernamePasswordAuthenticationFilter.class
                )
                .headers(headers -> headers
                    .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )
                .build();
    }

    /**
     * This method is used to create an {@code AuthenticationManager} bean.
     *
     * @return the created {@code AuthenticationManager} bean.
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(this.authenticationProvider());
    }

    /**
     * This method is used to create a {@code UserDetailsService} bean.
     *
     * @return The {@code UserDetailsService} object.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return this.appUserDetailsService;
    }

    /**
     * This method is used to create an {@code AuthenticationProvider} bean.
     *
     * @return The {@code AuthenticationProvider} object.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(this.userDetailsService());
        provider.setPasswordEncoder(this.passwordEncoder());
        return provider;
    }

    /**
     * The password encoder bean.
     *
     * @return a {@code BCryptPasswordEncoder} object.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
