package com.github.andreyelagin.spotifyplay.upstream;

import com.neovisionaries.i18n.CountryCode;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.enums.ModelObjectType;
import com.wrapper.spotify.model_objects.specification.AlbumSimplified;
import com.wrapper.spotify.model_objects.specification.Artist;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.PagingCursorbased;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SpotifyClient {

  private final SpotifyApi api;

  public Mono<PagingCursorbased<Artist>> followedArtists(Optional<String> lastArtistId) {
    var builder = api.getUsersFollowedArtists(ModelObjectType.ARTIST);
    lastArtistId.map(builder::after);
    return Mono
        .fromFuture(builder.build().executeAsync())
        .delayElement(Duration.ofMillis(100));
  }

  public Mono<Paging<AlbumSimplified>> artistAlbums(String artistId, int offset) {
    return Mono.fromFuture(() -> api
        .getArtistsAlbums(artistId)
        .market(CountryCode.RU)
        .album_type("album")
        .offset(offset)
        .build()
        .executeAsync());
  }
}
