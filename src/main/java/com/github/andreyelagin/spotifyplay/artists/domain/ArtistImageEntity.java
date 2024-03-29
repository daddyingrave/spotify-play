package com.github.andreyelagin.spotifyplay.artists.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table("artists_images")
@AllArgsConstructor
public class ArtistImageEntity {
  @Column("artist_id")
  String artistId;
  @Column("image_id")
  Long imageId;
}
