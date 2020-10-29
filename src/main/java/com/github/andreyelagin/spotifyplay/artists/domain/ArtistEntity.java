package com.github.andreyelagin.spotifyplay.artists.domain;

import com.github.andreyelagin.spotifyplay.domain.ExternalUrl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.isEmpty;

@Value
@Table("artists")
@Builder
@AllArgsConstructor
public class ArtistEntity {
  @Id
  String id;

  String name;

  @Column("external_urls")
  List<ExternalUrl> externalUrls;

  int followers;

  List<String> genres;

  String href;

  long imageId;

  short popularity;

  String uri;

  @PersistenceConstructor
  public ArtistEntity(
      String id,
      String name,
      String[][] externalUrls,
      int followers,
      String[] genres,
      String href,
      long imageId,
      short popularity,
      String uri
  ) {
    this.id = id;
    this.name = name;
    this.externalUrls = Arrays
        .stream(externalUrls)
        .filter(arr -> arr.length == 2 && !isEmpty(arr[0]) && !isEmpty(arr[1]))
        .map(arr -> new ExternalUrl(arr[0], arr[1]))
        .collect(Collectors.toList());
    this.followers = followers;
    this.genres = genres != null ? Arrays.asList(genres.clone()) : List.of();
    this.href = href;
    this.imageId = imageId;
    this.popularity = popularity;
    this.uri = uri;
  }

  public String[][] urlsAsArray() {
    var arr = new String[externalUrls.size()][];
    for (int i = 0; i < externalUrls.size(); i++) {
      var url = externalUrls.get(i);
      arr[i] = new String[]{url.getName(), url.getUrl()};
    }
    return arr;
  }
}
