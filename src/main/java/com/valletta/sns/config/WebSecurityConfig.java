package com.valletta.sns.config;

import static org.springframework.security.config.Customizer.withDefaults;

import com.valletta.sns.config.filter.JwtTokenFilter;
import com.valletta.sns.exception.CustomAuthenticationEntryPoint;
import com.valletta.sns.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
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
    };

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring().requestMatchers("/예외처리하고 싶은 url", "/예외처리하고 싶은 url");
        return (web) -> web.ignoring()
            .requestMatchers("^(?!/api/).*")
            .requestMatchers("/api/**")
            .requestMatchers("/swagger", "/swagger-ui.html", "/swagger-ui/**", "/api-docs", "/api-docs/**", "/v3/api-docs/**")
            .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    protected SecurityFilterChain config(HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable) // RestAPI는 csrf 보안이 필요 없으므로 비활성화
            .cors(withDefaults())
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable) // RestAPI는 UI를 사용하지 않기 때문에, 기본설정을 비활성화
            .authorizeHttpRequests(auth -> auth
//                .requestMatchers("/swagger", "/swagger-ui.html", "/swagger-ui/**", "/api-docs", "/api-docs/**", "/v3/api-docs/**")
//                .permitAll()
                .requestMatchers(WHITE_LIST)
                .permitAll()
//                .requestMatchers("/api/**")
//                .permitAll()
                .anyRequest()
                .authenticated()
            )
            .addFilterBefore(new JwtTokenFilter(key, userService), UsernamePasswordAuthenticationFilter.class)
            .sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // JwtToken 인증방식이므로 세션을 사용하지 않으므로 비활성화
            .exceptionHandling((exceptionConfig) ->
                exceptionConfig.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
            )
            .build();


    }
}