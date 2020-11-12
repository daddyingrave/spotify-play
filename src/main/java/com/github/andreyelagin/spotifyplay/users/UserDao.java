package com.github.andreyelagin.spotifyplay.users;

import com.github.andreyelagin.spotifyplay.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserDao {

  private final DatabaseClient databaseClient;
  private final TransactionalOperator operator;

  public Mono<User> createUser(User user) {
    return databaseClient
        .execute("""
                INSERT INTO users (name, email, token, refresh_token, expires_in) 
                VALUES (:name, :email, :token, :refresh_token, :expires_in)
            """)
        .bind("name", user.getName())
        .bind("email", user.getEmail())
        .bind("token", user.getToken())
        .bind("refresh_token", user.getRefreshToken())
        .bind("expires_in", user.getExpiresIn())
        .map(row -> user.toBuilder().id(row.get("id", Long.class)).build())
        .one()
        .as(operator::transactional);
  }
}
