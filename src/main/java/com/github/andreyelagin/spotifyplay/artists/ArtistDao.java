package com.github.andreyelagin.spotifyplay.artists;

import com.github.andreyelagin.spotifyplay.artists.domain.ArtistEntity;
import com.github.andreyelagin.spotifyplay.artists.domain.ArtistImageEntity;
import com.github.andreyelagin.spotifyplay.artists.domain.ImageEntity;
import com.wrapper.spotify.model_objects.specification.Artist;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ArtistDao {

  private final DatabaseClient databaseClient;
  private final TransactionalOperator operator;

  public Mono<String> persist(Artist artist) {
    var images = Arrays.stream(artist.getImages())
        .map(ImageEntity::fromSpotify)
        .collect(Collectors.toList());

    return Flux
        .zip(insertArtist(ArtistsMapper.toArtist(artist)), insertImages(images))
        .flatMap(t -> Flux
            .fromIterable(t.getT2()
                .stream()
                .map(imageId -> ArtistImageEntity
                    .builder()
                    .artistId(t.getT1())
                    .imageId(imageId)
                    .build()
                )
                .collect(Collectors.toList())))
        .flatMap(i -> databaseClient
            .insert()
            .into(ArtistImageEntity.class)
            .using(i)
            .fetch()
            .all()
        )
        .then(Mono.just(artist.getId()))
        .as(operator::transactional);
  }


  private Mono<String> insertArtist(ArtistEntity artist) {
    return databaseClient
        .insert()
        .into(ArtistEntity.class)
        .using(artist)
        .map(row -> row.get("id", String.class))
        .one();
  }

  private Mono<List<Long>> insertImages(List<ImageEntity> images) {
    return databaseClient
        .insert()
        .into(ImageEntity.class)
        .using(Flux.fromIterable(images))
        .map(row -> row.get("id", Long.class))
        .all()
        .collectList();
  }
}
