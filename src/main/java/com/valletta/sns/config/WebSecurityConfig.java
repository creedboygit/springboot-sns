package com.valletta.sns.config;

import com.valletta.sns.config.filter.JwtTokenFilter;
import com.valletta.sns.exception.CustomAuthenticationEntryPoint;
import com.valletta.sns.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final UserService userService;

    @Value("${jwt.secret-key}")
    private String key;

    private static final String[] WHITE_LIST = {
        "/api/*/users/join",
        "/api/*/users/login",
//        "/api/*/posts",
//        "/**"
    };

    @Bean
    protected SecurityFilterChain config(HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers(WHITE_LIST).permitAll()
                    .requestMatchers("/api/**").authenticated()
//                .anyRequest().authenticated()
            )
            .addFilterBefore(new JwtTokenFilter(key, userService), UsernamePasswordAuthenticationFilter.class)
            .sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .httpBasic(Customizer.withDefaults())
            .exceptionHandling((exceptionConfig) ->
                exceptionConfig.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
            )
            .build();
    }
}