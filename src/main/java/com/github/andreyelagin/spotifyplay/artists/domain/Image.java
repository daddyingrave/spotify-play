package com.github.andreyelagin.spotifyplay.artists.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("images")
@Builder
@AllArgsConstructor
public class Image {
  @Id
  Long id;
  int height;
  int width;
  String url;
}
