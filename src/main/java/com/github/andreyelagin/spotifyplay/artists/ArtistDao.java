package com.github.andreyelagin.spotifyplay.artists;

import com.github.andreyelagin.spotifyplay.artists.domain.ArtistEntity;
import com.github.andreyelagin.spotifyplay.artists.domain.ArtistImageEntity;
import com.github.andreyelagin.spotifyplay.artists.domain.ImageEntity;
import com.wrapper.spotify.model_objects.specification.Artist;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.ConnectionAccessor;
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
  private final ConnectionAccessor connectionAccessor;
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
        .flatMap(this::insertArtistImages)
        .then(Mono.just(artist.getId()))
        .as(operator::transactional);
  }


  private Mono<String> insertArtist(ArtistEntity artist) {
    return databaseClient
        .execute("""
            INSERT INTO artists (id, name, external_urls, followers, genres, href, popularity, uri)
            VALUES (:id, :name, :external_urls, :followers, :genres, :href, :popularity, :uri)
            ON CONFLICT DO NOTHING
            RETURNING id
            """
        )
        .bind("id", artist.getId())
        .bind("name", artist.getName())
        .bind("external_urls", new String[][]{{"", ""}})
        .bind("followers", artist.getFollowers())
        .bind("genres", artist.getGenres().toArray(new String[0]))
        .bind("href", artist.getHref())
        .bind("popularity", artist.getPopularity())
        .bind("uri", artist.getPopularity())
        .map(row -> row.get("id", String.class))
        .one();
  }

  private Mono<List<Long>> insertImages(List<ImageEntity> images) {
    if (images.isEmpty()) {
      return Mono.empty();
    } else {
      return connectionAccessor
          .inConnectionMany(connection -> {
            var statement = connection.createStatement("""
                INSERT INTO images (height, width, url)
                VALUES ($1, $2, $3)
                ON CONFLICT DO NOTHING 
                RETURNING id
                """);
            images.forEach(image -> statement
                .bind(0, image.getHeight())
                .bind(1, image.getWidth())
                .bind(2, image.getUrl())
                .add()
            );
            return Flux
                .from(statement.execute())
                .flatMap(result -> result.map((row, m) -> row.get("id", Long.class)));
          })
          .collectList();
    }
  }

  private Mono<Void> insertArtistImages(ArtistImageEntity artistImage) {
    return databaseClient
        .execute("""
            INSERT INTO artists_images (artist_id, image_id)
            VALUES (:artist_id, :image_id)
            ON CONFLICT DO NOTHING
            """
        )
        .bind("artist_id", artistImage.getArtistId())
        .bind("image_id", artistImage.getImageId())
        .fetch()
        .one()
        .then();
  }
}
