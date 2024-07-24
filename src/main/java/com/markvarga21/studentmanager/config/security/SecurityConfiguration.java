package com.markvarga21.studentmanager.config.security;

import com.markvarga21.studentmanager.exception.handler.security.AppAccessDeniedHandler;
import com.markvarga21.studentmanager.service.auth.security.AppUserDetailsService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration class for setting up the security.
 */
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
    /**
     * The base url for authentication services.
     */
    static final String BASE_AUTH_URL = "/api/v1/auth";

    /**
     * The AppUserDetailsService object.
     */
    private final AppUserDetailsService appUserDetailsService;

    /**
     * The AppAccessDeniedHandler object.
     */
    private final AppAccessDeniedHandler appAccessDeniedHandler;

    /**
     * The authentication entrypoint of the application.
     */
    private final AppAuthenticationEntryPoint entryPoint;

    /**
     * The application context.
     */
    private final ApplicationContext applicationContext;

    /**
     * The logout success handler.
     */
    private final AppLogoutSuccessHandler logoutSuccessHandler;

    /**
     * This method is used to configure the security filter chain.
     *
     * @param httpSecurity The HttpSecurity object to configure the security filter chain.
     * @return The SecurityFilterChain object.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity httpSecurity) throws Exception {
        JwtAuthFilter jwtAuthFilter = this.applicationContext
                .getBean(JwtAuthFilter.class);

        String[] permittedEndpoints = {
                BASE_AUTH_URL + "/register",
                BASE_AUTH_URL + "/login",
                BASE_AUTH_URL + "/logout",
        };
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
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
                ).logout(logout -> logout
                    .logoutUrl(BASE_AUTH_URL + "/logout")
                    .logoutSuccessHandler(this.logoutSuccessHandler)
                ).build();
    }

    /**
     * This method is used to create an AuthenticationManager bean.
     *
     * @return the created AuthenticationManager bean.
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(this.authenticationProvider());
    }

    /**
     * This method is used to create a UserDetailsService object.
     *
     * @return The UserDetailsService object.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return this.appUserDetailsService;
    }

    /**
     * This method is used to create an AuthenticationProvider object.
     *
     * @return The AuthenticationProvider object.
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
     * @return a BCryptPasswordEncoder object.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
