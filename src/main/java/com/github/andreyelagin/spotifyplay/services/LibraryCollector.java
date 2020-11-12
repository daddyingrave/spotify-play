package com.github.andreyelagin.spotifyplay.services;

import com.github.andreyelagin.spotifyplay.allbums.AlbumsDao;
import com.github.andreyelagin.spotifyplay.artists.ArtistDao;
import com.github.andreyelagin.spotifyplay.upstream.AlbumsSpotifyApi;
import com.github.andreyelagin.spotifyplay.upstream.ArtistsSpotifyApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class LibraryCollector {

  private final ArtistsSpotifyApi artistsSpotifyApi;
  private final AlbumsSpotifyApi albumsSpotifyApi;
  private final ArtistDao artistDao;
  private final AlbumsDao albumsDao;

  public Mono<Void> collectUserLibrary(Long userId) {
    return artistsSpotifyApi
        .getUserArtists()
        .flatMap(artistDao::persist)
        .delayElements(Duration.ofMillis(100))
        .flatMap(albumsSpotifyApi::getArtistAlbums)
        .flatMap(albumsDao::persistAlbum)
        .then(Mono.empty());
  }
}
