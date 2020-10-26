package com.github.andreyelagin.spotifyplay.allbums.domain;

import com.github.andreyelagin.spotifyplay.domain.ExternalUrl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.isEmpty;

@Value
@Table("albums")
@Builder
@AllArgsConstructor
public class AlbumEntity {
  @Id
  String id;

  @Column("albums_group")
  String albumsGroup;

  @Column("album_type")
  String albumType;

  @Column("available_markets")
  List<String> availableMarkets;

  @Column("external_urls")
  List<ExternalUrl> externalUrls;

  String href;

  String name;

  String type;

  String uri;

  @PersistenceConstructor
  public AlbumEntity(
      String id,
      String albumsGroup,
      String albumType,
      String[] availableMarkets,
      String[][] externalUrls,
      String href,
      String name,
      String type,
      String uri
  ) {
    this.id = id;
    this.albumsGroup = albumsGroup;
    this.albumType = albumType;
    this.availableMarkets = Arrays.asList(availableMarkets);
    this.externalUrls = Arrays
        .stream(externalUrls)
        .filter(arr -> arr.length == 2 && !isEmpty(arr[0]) && !isEmpty(arr[1]))
        .map(arr -> new ExternalUrl(arr[0], arr[1]))
        .collect(Collectors.toList());
    this.href = href;
    this.name = name;
    this.type = type;
    this.uri = uri;
  }

}
