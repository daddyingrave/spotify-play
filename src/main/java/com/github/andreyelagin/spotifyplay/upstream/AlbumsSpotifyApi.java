package com.github.andreyelagin.spotifyplay.upstream;

import com.wrapper.spotify.model_objects.specification.AlbumSimplified;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.isEmpty;

@Component
@RequiredArgsConstructor
public class AlbumsSpotifyApi {

  private final SpotifyClient client;

  public Flux<AlbumSimplified> getArtistAlbums(String artistId) {
    return client.artistAlbums(artistId, 0)
        .expand(albums -> {
          if (isEmpty(albums.getNext())) {
            return Flux.empty();
          } else {
            return client.artistAlbums(artistId, albums.getItems().length);
          }
        })
        .limitRequest(1)
        .flatMap(albums -> Flux.fromIterable(Arrays
            .stream(albums.getItems())
            .collect(Collectors.toList())));
  }
}
