package com.fransua.gateway.config;

import com.fransua.gateway.user.OAuth2UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@SpringBootConfiguration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableRedisHttpSession
public class SecutiryConfig {

  private final OAuth2UserService customerOAuth2UserService;

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
    requestHandler.setCsrfRequestAttributeName(null);

    http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .csrf(
            csrf ->
                csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    .csrfTokenRequestHandler(requestHandler))
        .addFilterAfter(new CsrfCookieFilter(), CsrfFilter.class)
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(
                        "/ping",
                        "/login/**",
                        "/oauth2/**",
                        "/error",
                        "/favicon.ico",
                        "/login-error")
                    .permitAll()
                    .requestMatchers("/api/order/**", "/api/burger/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .exceptionHandling(
            exceptions ->
                exceptions.defaultAuthenticationEntryPointFor(
                    new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                    request -> request.getRequestURI().startsWith("/api")))
        .oauth2Login(
            oauth2 ->
                oauth2
                    .defaultSuccessUrl("/", false)
                    .failureUrl("/login-error")
                    .userInfoEndpoint(userInfo -> userInfo.userService(customerOAuth2UserService)));

    return http.build();
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of("http://localhost", "http://127.0.0.1"));
    configuration.setAllowedMethods(List.of("*"));
    configuration.setAllowedHeaders(List.of("*"));
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
