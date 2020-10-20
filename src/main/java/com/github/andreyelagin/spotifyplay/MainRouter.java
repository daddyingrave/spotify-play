package com.github.andreyelagin.spotifyplay;

import com.github.andreyelagin.spotifyplay.artists.ArtistsHandler;
import com.github.andreyelagin.spotifyplay.handlers.MainHandler;
import com.github.andreyelagin.spotifyplay.handlers.UserHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
@RequiredArgsConstructor
public class MainRouter {

  private final MainHandler mainHandler;
  private final UserHandler userHandler;
  private final ArtistsHandler artistsHandler;

  @Bean
  public RouterFunction<ServerResponse> route() {
    return RouterFunctions.route()
        .GET("/test", accept(MediaType.ALL), mainHandler::test)

        .GET("/users", accept(MediaType.ALL), userHandler::getAllUsers)
        .POST("/users", accept(MediaType.APPLICATION_JSON), userHandler::createUser)

        .GET("/artists", accept(MediaType.ALL), artistsHandler::getAllArtists)
        .build();
  }
}
