package com.github.andreyelagin.spotifyplay;

import com.github.andreyelagin.spotifyplay.upstream.AlbumsSpotifyApi;
import com.github.andreyelagin.spotifyplay.upstream.SpotifyClient;
import com.wrapper.spotify.model_objects.specification.AlbumSimplified;
import com.wrapper.spotify.model_objects.specification.Paging;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MainRouterTest {
  @Autowired
  private AlbumsSpotifyApi albumsSpotifyApi;

  @MockBean
  private SpotifyClient client;

  @Before
  public void before() {
    var paging = new Paging.Builder<AlbumSimplified>().setNext("piotr");
    var paging2 = new Paging.Builder<AlbumSimplified>();
    var albums = new AlbumSimplified[]{
        new AlbumSimplified.Builder().setName("vasian").build(),
    };
    var albums2 = new AlbumSimplified[]{
        new AlbumSimplified.Builder().setName("piotr").build()
    };

    Mockito
        .when(client.artistAlbums(anyString(), anyInt()))
        .thenReturn(Mono.just(paging.setItems(albums).build()))
        .thenReturn(Mono.just(paging2.setItems(albums2).build()));
  }

  @Test
  @DisplayName("Beautiful test")
  public void test1() {
    StepVerifier.create(albumsSpotifyApi.getArtistAlbums("vasian"))
        .expectSubscription()
        .expectNext(new AlbumSimplified.Builder().setName("vasian").build())
        .expectNext(new AlbumSimplified.Builder().setName("piotr").build())
        .verifyComplete();
  }
}
