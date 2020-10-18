package com.github.andreyelagin.spotifyplay;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class MainRouter {

  @Bean
  public RouterFunction<ServerResponse> route(MainHandler mainHandler) {
    return RouterFunctions
        .route(
            GET("/test").and(accept(MediaType.TEXT_PLAIN)),
            mainHandler::test
        );

  }
}
