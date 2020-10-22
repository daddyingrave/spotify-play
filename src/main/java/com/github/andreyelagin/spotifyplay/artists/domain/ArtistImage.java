package com.github.andreyelagin.spotifyplay.artists.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("artist_images")
@Builder
@AllArgsConstructor
public class ArtistImage {
  @Column("artist_id")
  String artistId;
  @Column("image_id")
  Long imageId;
}
