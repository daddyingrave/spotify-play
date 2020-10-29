package com.github.andreyelagin.spotifyplay.configuration;

import com.github.andreyelagin.spotifyplay.artists.domain.ArtistWritingConverter;
import com.wrapper.spotify.enums.ReleaseDatePrecision;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.postgresql.codec.EnumCodec;
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
public class DatabaseConfiguration extends AbstractR2dbcConfiguration {

  @Bean
  ReactiveTransactionManager transactionManager(ConnectionFactory connectionFactory) {
    return new R2dbcTransactionManager(connectionFactory);
  }

  @Bean
  @Override
  public ConnectionFactory connectionFactory() {
    PostgresqlConnectionConfiguration conf = PostgresqlConnectionConfiguration
        .builder()
        .host("localhost")
        .database("baza_vasiana")
        .username("spotify")
        .password("spotify")
        .codecRegistrar(EnumCodec
            .builder()
            .withEnum("date_precision", ReleaseDatePrecision.class)
            .build()
        ).build();
    return new PostgresqlConnectionFactory(conf);
  }

  @Override
  protected List<Object> getCustomConverters() {
    List<Object> converterList = new ArrayList<>();
    converterList.add(new ArtistWritingConverter());
    return converterList;
  }
}
