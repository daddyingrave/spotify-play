package com.github.andreyelagin.spotifyplay.repositories;

import com.github.andreyelagin.spotifyplay.domain.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UsersRepository extends ReactiveCrudRepository<User, Long> {

  Mono<User> findByEmail(String email);
}
