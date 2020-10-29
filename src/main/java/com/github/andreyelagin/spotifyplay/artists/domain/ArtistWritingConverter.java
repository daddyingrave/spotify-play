package com.github.andreyelagin.spotifyplay.artists.domain;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.data.r2dbc.mapping.SettableValue;

@WritingConverter
public class ArtistWritingConverter implements Converter<ArtistEntity, OutboundRow> {

  @Override
  public OutboundRow convert(ArtistEntity artistEntity) {
    var row = new OutboundRow();
    row.put("id", SettableValue.from(artistEntity.getId()));
    row.put("name", SettableValue.from(artistEntity.getName()));
//    row.put("external_urls", SettableValue.from(artist.getExternalUrls().toArray()));
    row.put("external_urls", SettableValue.from(new String[][]{{"vasian", "rock"}}));
    row.put("followers", SettableValue.from(artistEntity.getFollowers()));
    row.put("genres", SettableValue.from(artistEntity.getGenres().toArray(new String[0])));
    row.put("href", SettableValue.from(artistEntity.getHref()));
    row.put("popularity", SettableValue.from(artistEntity.getPopularity()));
    row.put("uri", SettableValue.from(artistEntity.getUri()));
    return row;
  }
}
