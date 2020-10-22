package com.github.andreyelagin.spotifyplay.handlers;

import com.github.andreyelagin.spotifyplay.artists.ArtistImageRepository;
import com.github.andreyelagin.spotifyplay.artists.ArtistsMapper;
import com.github.andreyelagin.spotifyplay.artists.ArtistsRepository;
import com.github.andreyelagin.spotifyplay.artists.ImageRepository;
import com.github.andreyelagin.spotifyplay.artists.domain.ArtistImage;
import com.github.andreyelagin.spotifyplay.artists.domain.Image;
import com.github.andreyelagin.spotifyplay.upstream.AlbumsSpotifyApi;
import com.github.andreyelagin.spotifyplay.upstream.ArtistsSpotifyApi;
import com.wrapper.spotify.model_objects.specification.AlbumSimplified;
import com.wrapper.spotify.model_objects.specification.Artist;
import lombok.RequiredArgsConstructor;
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

  public Mono<ServerResponse> test(ServerRequest request) {
    var multicastFlux = artistsSpotifyApi
        .getUserArtists()
        .delayElements(Duration.ofMillis(100))
        .share();
    Flux.from(multicastFlux)
        .flatMap(a -> Flux
            .just(Arrays.asList(a.getImages().clone()))
            .map(image -> image
                .stream()
                .map(i -> Image
                    .builder()
                    .height(i.getHeight())
                    .width(i.getWidth())
                    .url(i.getUrl())
                    .build()
                )
                .collect(Collectors.toList())

            )
            .flatMap(images -> Flux.zip(
                artistsRepository.save(ArtistsMapper.toArtist(a)),
                imageRepository.saveAll(images).collectList())
            )
        )
        .subscribe(t -> artistImageRepository
            .saveAll(t.getT2().stream()
                .map(i -> ArtistImage
                    .builder()
                    .artistId(t.getT1().getId())
                    .imageId(i.getId())
                    .build()
                )
                .collect(Collectors.toList())));
    return ServerResponse
        .ok()
        .body(multicastFlux
            .map(Artist::getId)
            .flatMap(albumsSpotifyApi::getArtistAlbums), AlbumSimplified.class
        );
  }
}
