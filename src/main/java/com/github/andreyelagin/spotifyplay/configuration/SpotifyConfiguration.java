package com.github.andreyelagin.spotifyplay.configuration;

import com.wrapper.spotify.SpotifyApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpotifyConfiguration {

  @Value("${spotify.token}")
  private String spotifyToken;

  @Bean
  public SpotifyApi spotifyApi() {
    return new SpotifyApi
        .Builder()
        .setAccessToken(spotifyToken)
        .build();
  }
}
