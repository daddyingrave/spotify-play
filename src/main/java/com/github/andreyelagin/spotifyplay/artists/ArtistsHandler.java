package com.github.andreyelagin.spotifyplay.artists;

import com.github.andreyelagin.spotifyplay.artists.domain.ArtistEntity;
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
        .body(artistsRepository.findAll(), ArtistEntity.class);
  }
}
