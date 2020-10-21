package com.github.andreyelagin.spotifyplay.artists;

import com.github.andreyelagin.spotifyplay.artists.domain.Artist;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ArtistsRepository extends ReactiveCrudRepository<Artist, Long> {

  @Query("""
      SELECT id, name, external_urls, followers, genres, href, image_id, popularity, uri 
      FROM artists
      """)
  Flux<Artist> getAllArtists();

  /// todo
  @Query("""
      INSERT INTO artists 
        (id, name, external_urls, followers, genres, href, image_id, popularity, uri)
      VALUES 
        (
          :#{#artist.id},
          :#{#artist.name},
          :#{#artist.external_urls},
          :#{#artist.followers},
          :#{#artist.genres},
          :#{#artist.href},
          :#{#artist.image_id},
          :#{#artist.popularity},
          :#{#artist.uri}
        )
      """)
  Flux<Void> saveArtist(Artist artist);
}
