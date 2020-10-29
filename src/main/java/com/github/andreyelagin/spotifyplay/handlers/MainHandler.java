package com.github.andreyelagin.spotifyplay.handlers;

import com.github.andreyelagin.spotifyplay.allbums.AlbumsDao;
import com.github.andreyelagin.spotifyplay.artists.ArtistDao;
import com.github.andreyelagin.spotifyplay.upstream.AlbumsSpotifyApi;
import com.github.andreyelagin.spotifyplay.upstream.ArtistsSpotifyApi;
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
  private final ArtistDao artistDao;
  private final AlbumsDao albumsDao;

  public Mono<ServerResponse> test(ServerRequest request) {
    var resp = artistsSpotifyApi
        .getUserArtists()
        .flatMap(artistDao::persist)
        .delayElements(Duration.ofMillis(100))
        .flatMap(albumsSpotifyApi::getArtistAlbums)
        .flatMap(albumsDao::persistAlbum)
        .then(Mono.just("My job is done here..."));

    return ServerResponse
        .ok()
        .body(resp, String.class);
  }
}
