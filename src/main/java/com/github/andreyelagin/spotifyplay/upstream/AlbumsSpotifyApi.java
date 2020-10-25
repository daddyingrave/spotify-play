package com.github.andreyelagin.spotifyplay.upstream;

import com.wrapper.spotify.exceptions.detailed.TooManyRequestsException;
import com.wrapper.spotify.model_objects.specification.AlbumSimplified;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

import java.time.Duration;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.isEmpty;

@Component
@RequiredArgsConstructor
public class AlbumsSpotifyApi {

  private final SpotifyClient client;

  private static final RetryBackoffSpec BACKOFF_SPEC = Retry
      .backoff(3, Duration.ofSeconds(5))
      .filter(e -> e instanceof TooManyRequestsException);

  public Flux<AlbumSimplified> getArtistAlbums(String artistId) {
    return Mono
        .zip(Mono.just(0), client.artistAlbums(artistId, 0).retryWhen(BACKOFF_SPEC))
        .expand(t -> {
          var albums = t.getT2();
          var offset = t.getT1() + albums.getTotal() - 1;
          if (isEmpty(albums.getNext())) {
            return Mono.empty();
          } else {
            return Mono.zip(
                Mono.just(offset),
                client.artistAlbums(artistId, offset).retryWhen(BACKOFF_SPEC)
            );
          }
        })
        .flatMap(t -> Flux.fromIterable(Arrays
            .stream(t.getT2().getItems())
            .collect(Collectors.toList())));
  }
}
