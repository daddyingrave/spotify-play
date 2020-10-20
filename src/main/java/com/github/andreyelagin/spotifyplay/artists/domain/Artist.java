package com.github.andreyelagin.spotifyplay.artists.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Value
@Table("artists")
public class Artist {
  @Id
  String id;
  String name;
  List<String> genres;
  List<Pair> external_urls;

  @PersistenceConstructor
  protected Artist(String id, String name, String[][] external_urls, String[] genres) {
    this.id = id;
    this.name = name;
    this.genres = Arrays.asList(genres.clone());
    this.external_urls = Arrays.stream(external_urls).map(arr -> new Pair(arr[0], arr[1])).collect(Collectors.toList());
    System.out.println(Arrays.deepToString(external_urls));
  }
}

@Value
class Pair {
  String key;
  String value;
}
