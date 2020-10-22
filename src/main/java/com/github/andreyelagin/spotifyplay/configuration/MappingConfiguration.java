package com.github.andreyelagin.spotifyplay.configuration;

import com.github.andreyelagin.spotifyplay.artists.domain.ArtistWritingConverter;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class MappingConfiguration extends AbstractR2dbcConfiguration {

  public ConnectionFactory connectionFactory() {
    return ConnectionFactories.get(ConnectionFactoryOptions.builder().build());
  }


  @Override
  protected List<Object> getCustomConverters() {

    List<Object> converterList = new ArrayList<>();
    converterList.add(new ArtistWritingConverter());
    return converterList;
  }
}
