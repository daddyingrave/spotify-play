package com.github.andreyelagin.spotifyplay.artists.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("artists")
public class Artist {
  @Id
  private String id;
  private String name;
  private List<String> generes;
}
