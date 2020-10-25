package com.github.andreyelagin.spotifyplay.artists;

import com.github.andreyelagin.spotifyplay.artists.domain.ImageEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ImageRepository extends ReactiveCrudRepository<ImageEntity, Long> {
}
