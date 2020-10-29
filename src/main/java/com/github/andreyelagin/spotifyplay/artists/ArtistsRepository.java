package com.github.andreyelagin.spotifyplay.artists;

import com.github.andreyelagin.spotifyplay.artists.domain.ArtistEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ArtistsRepository extends ReactiveCrudRepository<ArtistEntity, Long> {
}
