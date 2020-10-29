package com.github.andreyelagin.spotifyplay.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class ExternalUrl {
  String name;
  String url;
}
