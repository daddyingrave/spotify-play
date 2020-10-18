package com.github.andreyelagin.spotifyplay.handlers;

import com.github.andreyelagin.spotifyplay.domain.User;
import com.github.andreyelagin.spotifyplay.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserHandler {

  private final UserRepository userRepository;

  public Mono<ServerResponse> getAllUsers(ServerRequest request) {
    return ServerResponse
        .ok()
        .body(userRepository.getAllUsers(), User.class);
  }

  public Mono<ServerResponse> createUser(ServerRequest request) {
    return request.bodyToMono(User.class)
        .flatMap(u -> ServerResponse
            .ok()
            .body(userRepository.insertUser(u), Void.class)
        );
  }
}
