package com.github.andreyelagin.spotifyplay.handlers;

import com.github.andreyelagin.spotifyplay.artists.ArtistImageRepository;
import com.github.andreyelagin.spotifyplay.artists.ArtistsMapper;
import com.github.andreyelagin.spotifyplay.artists.ArtistsRepository;
import com.github.andreyelagin.spotifyplay.artists.ImageRepository;
import com.github.andreyelagin.spotifyplay.artists.domain.ArtistImageEntity;
import com.github.andreyelagin.spotifyplay.artists.domain.ImageEntity;
import com.github.andreyelagin.spotifyplay.upstream.AlbumsSpotifyApi;
import com.github.andreyelagin.spotifyplay.upstream.ArtistsSpotifyApi;
import com.wrapper.spotify.model_objects.specification.AlbumSimplified;
import com.wrapper.spotify.model_objects.specification.Artist;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MainHandler {

  private final ArtistsSpotifyApi artistsSpotifyApi;
  private final AlbumsSpotifyApi albumsSpotifyApi;
  private final ArtistsRepository artistsRepository;
  private final ImageRepository imageRepository;
  private final ArtistImageRepository artistImageRepository;
  private final DatabaseClient databaseClient;

  public Mono<ServerResponse> test(ServerRequest request) {
    var huy = artistsSpotifyApi
        .getUserArtists()
        .flatMap(this::persistArtist)
        .map(Artist::getId)
        .delayElements(Duration.ofMillis(100))
        .flatMap(albumsSpotifyApi::getArtistAlbums);

    return ServerResponse
        .ok()
        .body(huy, AlbumSimplified.class);
  }

  private Flux<Artist> persistArtist(Artist artist) {
    return Flux
        .just(Arrays.asList(artist.getImages()))
        .map(image -> image
            .stream()
            .map(ImageEntity::fromSpotify)
            .collect(Collectors.toList())
        )
        .flatMap(images -> Flux.zip(
            artistsRepository.save(ArtistsMapper.toArtist(artist)),
            imageRepository.saveAll(images).collectList())
        )
        .map(t -> t.getT2()
        .stream()
        .map(i -> ArtistImageEntity
            .builder()
            .artistId(t.getT1().getId())
            .imageId(i.getId())
            .build())
        .collect(Collectors.toList()))
        .flatMap(t -> databaseClient
            .insert()
            .into(ArtistImageEntity.class)
            .using(Flux.fromIterable(t))
            .fetch()
            .all())
        .flatMap(m -> Flux.just(artist))
        .distinct();
  }
}
