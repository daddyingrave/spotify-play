package com.github.andreyelagin.spotifyplay.artists;

import com.github.andreyelagin.spotifyplay.artists.domain.Artist;
import com.github.andreyelagin.spotifyplay.domain.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ArtistsRepository extends ReactiveCrudRepository<Artist, Long> {

  @Query("SELECT id, name, genres FROM artists")
  Flux<Artist> getAllArtists();
}
