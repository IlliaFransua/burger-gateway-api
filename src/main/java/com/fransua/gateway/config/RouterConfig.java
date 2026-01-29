package com.fransua.gateway.config;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.*;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicate;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class RouterConfig {

  @Value("${BURGER_UI_HOST}")
  private String uiHost;

  @Value("${BURGER_UI_PORT}")
  private int uiPort;

  @Value("${BURGER_API_HOST}")
  private String apiHost;

  @Value("${BURGER_API_PORT}")
  private int apiPort;

  @Bean
  RouterFunction<ServerResponse> burgerOrderApi() {
    String destinationUri = "http://%s:%d".formatted(apiHost, apiPort);

    return route("burger_order_api")
        .route(path("/api/order/**", "/api/burger/**"), http())
        .before(uri(destinationUri))
        .build();
  }

  @Bean
  RouterFunction<ServerResponse> burgerOrderUi() {
    String destinationUri = "http://%s:%d".formatted(uiHost, uiPort);

    return route("burger_order_ui")
        .route(path("/**").and(path("/api/**").negate()).and(isInternalPath().negate()), http())
        .before(uri(destinationUri))
        .build();
  }

  private RequestPredicate isInternalPath() {
    return path("/ping", "/login/**", "/oauth2/**", "/error", "/favicon.ico");
  }
}
