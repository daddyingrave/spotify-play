package com.github.andreyelagin.spotifyplay.artists;

import com.github.andreyelagin.spotifyplay.artists.domain.Artist;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ArtistsRepository extends ReactiveCrudRepository<Artist, Long> {

  @Query("SELECT id, name, genres, external_urls FROM artists")
  Flux<Artist> getAllArtists();
}
