package com.github.andreyelagin.spotifyplay.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@Table("users")
public class User {
  @Id
  Long id;
  String name;
  String email;
  String token;
  String refreshToken;
  LocalDateTime expiresIn;
}
