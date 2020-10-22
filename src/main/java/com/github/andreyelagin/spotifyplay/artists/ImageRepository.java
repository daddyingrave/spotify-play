package com.github.andreyelagin.spotifyplay.artists;

import com.github.andreyelagin.spotifyplay.artists.domain.Image;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ImageRepository extends ReactiveCrudRepository<Image, Long> {
}
