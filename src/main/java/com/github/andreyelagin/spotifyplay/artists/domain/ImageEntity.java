package com.github.andreyelagin.spotifyplay.artists.domain;

import com.wrapper.spotify.model_objects.specification.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("images")
@Builder
@AllArgsConstructor
public class ImageEntity {
  @Id
  Long id;
  int height;
  int width;
  String url;

  public static ImageEntity fromSpotify(Image image) {
    return ImageEntity
        .builder()
        .height(image.getHeight())
        .width(image.getWidth())
        .url(image.getUrl())
        .build();
  }
}
