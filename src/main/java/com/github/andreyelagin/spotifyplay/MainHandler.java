package com.github.andreyelagin.spotifyplay;

import com.github.andreyelagin.spotifyplay.allbums.AlbumsService;
import com.github.andreyelagin.spotifyplay.artists.ArtistsService;
import com.wrapper.spotify.model_objects.specification.AlbumSimplified;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class MainHandler {

  private final ArtistsService artistsService;
  private final AlbumsService albumsService;

  public Mono<ServerResponse> test(ServerRequest request) {
    return ServerResponse
        .ok()
        .body(artistsService
            .getUserArtistsIds()
            .delayElements(Duration.ofMillis(100))
            .flatMap(albumsService::getArtistAlbums), AlbumSimplified.class
        );
  }
}
