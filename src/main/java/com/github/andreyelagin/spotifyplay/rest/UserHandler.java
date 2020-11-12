package com.github.andreyelagin.spotifyplay.rest;

import com.github.andreyelagin.spotifyplay.domain.User;
import com.github.andreyelagin.spotifyplay.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserHandler {

  private final UsersRepository usersRepository;

  public Mono<ServerResponse> createUser(ServerRequest request) {
    return request.bodyToMono(User.class)
        .flatMap(u -> ServerResponse
            .ok()
            .body(usersRepository.save(u), User.class)
        );
  }

  public Mono<ServerResponse> getUser(ServerRequest request) {
    return Mono
        .just(request.queryParam("email").orElseThrow())
        .flatMap(email ->
            ServerResponse
                .ok()
                .body(usersRepository.findByEmail(email), User.class)
        );
  }
}
