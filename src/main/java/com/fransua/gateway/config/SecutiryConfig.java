package com.fransua.gateway.config;

import com.fransua.gateway.user.OAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootConfiguration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableRedisHttpSession
public class SecutiryConfig {

  private final OAuth2UserService customerOAuth2UserService;

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
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
}
