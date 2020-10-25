package com.github.andreyelagin.spotifyplay.configuration;

import com.github.andreyelagin.spotifyplay.artists.domain.ArtistWritingConverter;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.connectionfactory.R2dbcTransactionManager;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableTransactionManagement
public abstract class DatabaseConfiguration extends AbstractR2dbcConfiguration {

  @Bean
  ReactiveTransactionManager transactionManager(ConnectionFactory connectionFactory) {
    return new R2dbcTransactionManager(connectionFactory);
  }

  @Override
  protected List<Object> getCustomConverters() {
    List<Object> converterList = new ArrayList<>();
    converterList.add(new ArtistWritingConverter());
    return converterList;
  }
}
