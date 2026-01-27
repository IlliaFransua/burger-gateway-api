package com.fransua.gateway.config;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.*;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicate;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class RouterConfig {

  @Bean
  RouterFunction<ServerResponse> burgerOrderApi() {
    return route("burger_order_api")
        .route(path("/api/order/**", "/api/burger/**"), http())
        .before(uri("http://orders:8081"))
        .build();
  }

  @Bean
  RouterFunction<ServerResponse> burgerOrderUi() {
    return route("burger_order_ui")
        .route(path("/**").and(path("/api/**").negate()).and(isInternalPath().negate()), http())
        .before(uri("http://ui:8082"))
        .build();
  }

  private RequestPredicate isInternalPath() {
    return path("/ping", "/login/**", "/oauth2/**", "/error", "/favicon.ico");
  }
}
