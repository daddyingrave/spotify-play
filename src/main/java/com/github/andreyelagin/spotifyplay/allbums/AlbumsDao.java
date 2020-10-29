package com.github.andreyelagin.spotifyplay.allbums;

import com.neovisionaries.i18n.CountryCode;
import com.wrapper.spotify.model_objects.specification.AlbumSimplified;
import com.wrapper.spotify.model_objects.specification.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.ConnectionAccessor;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AlbumsDao {

  private final DatabaseClient databaseClient;
  private final ConnectionAccessor connectionAccessor;
  private final TransactionalOperator operator;

  public Mono<Void> persistAlbum(AlbumSimplified album) {
    return Flux
        .zip(insertAlbum(album), insertImages(album.getImages()))
        .flatMap(t -> Flux
            .fromIterable(t.getT2())
            .flatMap(imageId -> insertAlbumImages(t.getT1(), imageId))
        )
        .as(operator::transactional)
        .then();
  }

  private String[][] mapExternalUrls(Map<String, String> urls) {
    var arr = new String[urls.size()][];
    int i = 0;
    for (var entry : urls.entrySet()) {
      arr[i++] = new String[]{entry.getKey(), entry.getValue()};
    }
    return arr;
  }

  private Mono<String> insertAlbum(AlbumSimplified album) {
    var bind = databaseClient
        .execute("""
            INSERT INTO albums 
              (
                id, album_group, album_type, available_markets, 
                external_urls, href, name, type, uri, 
                release_date_precision, release_date
              )
            VALUES 
              (
                :id, :album_group, :album_type, :available_markets, 
                :external_urls, :href, :name, :type, :uri, 
                :release_date_precision, :release_date
              )
            ON CONFLICT DO NOTHING
            """
        )
        .bind("id", album.getId())
        .bind("album_group", album.getAlbumGroup().getGroup())
        .bind("album_type", album.getAlbumType().getType());

    if (album.getAvailableMarkets() == null) {
      bind = bind.bindNull("available_markets", String[].class);
    } else {
      List<String> list = new ArrayList<>();
      for (CountryCode countryCode : album.getAvailableMarkets()) {
        String alpha2 = countryCode.getAlpha2();
        list.add(alpha2);
      }
      bind = bind.bind("available_markets", list.toArray(new String[0]));
    }

    if (album.getType().getType() == null) {
      bind = bind.bindNull("type", String.class);
    } else {
      bind = bind.bind("type", album.getType().getType());
    }

    return bind
        .bind("external_urls", mapExternalUrls(album.getExternalUrls().getExternalUrls()))
        .bind("href", album.getHref())
        .bind("name", album.getName())
        .bind("uri", album.getUri())
        .bind("release_date_precision", album.getReleaseDatePrecision().getPrecision())
        .bind("release_date", album.getReleaseDate())
        .map(row -> row.get("id", String.class))
        .one();
  }

  private Mono<List<Long>> insertImages(Image[] images) {
    if (images.length == 0) {
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
            for (var image : images) {
              statement
                  .bind(0, image.getHeight())
                  .bind(1, image.getWidth())
                  .bind(2, image.getUrl())
                  .add();
            }
            return Flux
                .from(statement.execute())
                .flatMap(result -> result.map((row, m) -> row.get("id", Long.class)));
          })
          .collectList();
    }
  }

  private Mono<Void> insertAlbumImages(String albumId, Long imageId) {
    return databaseClient
        .execute("""
            INSERT INTO albums_images (album_id, image_id)
            VALUES (:artist_id, :image_id)
            ON CONFLICT DO NOTHING
            """
        )
        .bind("album_id", albumId)
        .bind("image_id", imageId)
        .fetch()
        .one()
        .then();
  }
}
