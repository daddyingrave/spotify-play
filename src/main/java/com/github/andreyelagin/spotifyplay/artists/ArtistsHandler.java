package com.github.andreyelagin.spotifyplay.artists;

import com.github.andreyelagin.spotifyplay.artists.domain.Artist;
import com.github.andreyelagin.spotifyplay.domain.User;
import com.github.andreyelagin.spotifyplay.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ArtistsHandler {

  private final ArtistsRepository artistsRepository;

  public Mono<ServerResponse> getAllArtists(ServerRequest request) {
    return ServerResponse
        .ok()
        .body(artistsRepository.getAllArtists(), Artist.class);
  }
}
