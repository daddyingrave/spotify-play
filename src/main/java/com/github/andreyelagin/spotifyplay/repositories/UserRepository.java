package com.github.andreyelagin.spotifyplay.repositories;

import com.github.andreyelagin.spotifyplay.domain.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {

  @Query("SELECT id, name, email, token FROM users")
  Flux<User> getAllUsers();

  @Query("""
          INSERT INTO users (name, email, token) 
          VALUES 
            (
              :#{#user.name},
              :#{#user.email},
              :#{#user.token}
            )
      """)
  Flux<Void> insertUser(@Param("user") User user);
}
