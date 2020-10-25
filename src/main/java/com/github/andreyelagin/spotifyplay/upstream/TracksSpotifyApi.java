package com.github.andreyelagin.spotifyplay.upstream;

import com.wrapper.spotify.exceptions.detailed.TooManyRequestsException;
import com.wrapper.spotify.model_objects.specification.AlbumSimplified;
import com.wrapper.spotify.model_objects.specification.TrackSimplified;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.isEmpty;

@Component
@RequiredArgsConstructor
public class TracksSpotifyApi {

  private final SpotifyClient client;

  public Flux<TrackSimplified> getAlbumTracks(String albumId) {
    return client.albumsTracks(albumId, 0)
        .expand(tracks -> {
          if (isEmpty(tracks.getNext())) {
            return Mono.empty();
          } else {
            return client.albumsTracks(albumId, tracks.getItems().length);
          }
        })
        .flatMap(tracks -> Flux.fromIterable(Arrays
            .stream(tracks.getItems())
            .collect(Collectors.toList())));
  }
}
