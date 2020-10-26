package com.github.andreyelagin.spotifyplay.allbums.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table("albums_images")
@AllArgsConstructor
public class AlbumImageEntity {
  @Column("album_id")
  String artistId;
  @Column("image_id")
  Long imageId;
}
