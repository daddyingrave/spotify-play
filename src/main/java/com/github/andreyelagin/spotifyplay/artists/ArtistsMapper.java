package com.github.andreyelagin.spotifyplay.artists;

import com.github.andreyelagin.spotifyplay.artists.domain.ArtistEntity;
import com.github.andreyelagin.spotifyplay.domain.ExternalUrl;
import com.wrapper.spotify.model_objects.specification.Artist;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ArtistsMapper {
  public static ArtistEntity toArtist(Artist artist) {
    return ArtistEntity.builder()
        .id(artist.getId())
        .name(artist.getName())
        .externalUrls(artist
            .getExternalUrls()
            .getExternalUrls()
            .entrySet()
            .stream()
            .map(p -> ExternalUrl
                .builder()
                .name(p.getKey())
                .url(p.getValue())
                .build())
            .collect(Collectors.toList())
        )
        .followers(artist.getFollowers().getTotal())
        .genres(Arrays.stream(artist.getGenres()).collect(Collectors.toList()))
        .href(artist.getHref())
        .popularity(artist.getPopularity().shortValue())
        .uri(artist.getUri())
        .build();
  }
}
