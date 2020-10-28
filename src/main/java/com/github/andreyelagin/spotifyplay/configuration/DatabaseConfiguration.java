package com.github.andreyelagin.spotifyplay.configuration;

import com.github.andreyelagin.spotifyplay.artists.domain.ArtistWritingConverter;
import com.wrapper.spotify.enums.ReleaseDatePrecision;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.connectionfactory.R2dbcTransactionManager;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.data.r2dbc.mapping.SettableValue;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableTransactionManagement
public class DatabaseConfiguration extends AbstractR2dbcConfiguration {

  @Bean
  ReactiveTransactionManager transactionManager(ConnectionFactory connectionFactory) {
    return new R2dbcTransactionManager(connectionFactory);
  }

  @Override
  public ConnectionFactory connectionFactory() {
    return null;
  }

  @Override
  protected List<Object> getCustomConverters() {
    List<Object> converterList = new ArrayList<>();
    converterList.add(new ArtistWritingConverter());
    converterList.add(new MyEnumTypeConverter());
    return converterList;
  }

  @WritingConverter
  class MyEnumTypeConverter implements Converter<ReleaseDatePrecision, OutboundRow> {

    @Override
    public OutboundRow convert(ReleaseDatePrecision source) {
      var row = new OutboundRow();
      row.put("release_date_precision", SettableValue.from(source.getPrecision()));
      return row;
    }
  }
}
