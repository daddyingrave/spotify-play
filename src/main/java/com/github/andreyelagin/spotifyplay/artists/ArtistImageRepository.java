package com.github.andreyelagin.spotifyplay.artists;

import com.github.andreyelagin.spotifyplay.artists.domain.ArtistImage;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ArtistImageRepository extends ReactiveCrudRepository<ArtistImage, Long> {
}
