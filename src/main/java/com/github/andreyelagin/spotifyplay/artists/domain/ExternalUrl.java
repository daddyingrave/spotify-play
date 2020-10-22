package com.github.andreyelagin.spotifyplay.artists.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ExternalUrl {
  String name;
  String url;
}
