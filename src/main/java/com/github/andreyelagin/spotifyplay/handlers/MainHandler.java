package com.github.andreyelagin.spotifyplay.handlers;

import com.github.andreyelagin.spotifyplay.upstream.AlbumsSpotifyApi;
import com.github.andreyelagin.spotifyplay.upstream.ArtistsSpotifyApi;
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

  private final ArtistsSpotifyApi artistsSpotifyApi;
  private final AlbumsSpotifyApi albumsSpotifyApi;

  public Mono<ServerResponse> test(ServerRequest request) {
    return ServerResponse
        .ok()
        .body(artistsSpotifyApi
            .getUserArtistsIds()
            .delayElements(Duration.ofMillis(100))
            .flatMap(albumsSpotifyApi::getArtistAlbums), AlbumSimplified.class
        );
  }
}
