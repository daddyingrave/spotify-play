package com.github.andreyelagin.spotifyplay.upstream;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.detailed.TooManyRequestsException;
import com.wrapper.spotify.model_objects.specification.AlbumSimplified;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Paging;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.util.StringUtils.isEmpty;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class AlbumsSpotifyApiTest {

  @Autowired
  private AlbumsSpotifyApi albumsSpotifyApi;

//  @MockBean
//  private SpotifyClient client;

  @BeforeEach
  void before() {
    Mockito.reset();

    var paging = new Paging.Builder<AlbumSimplified>().setNext("piotr");
    var paging2 = new Paging.Builder<AlbumSimplified>();
    var albums = new AlbumSimplified[]{
        new AlbumSimplified.Builder().setName("vasian").build(),
    };
    var albums2 = new AlbumSimplified[]{
        new AlbumSimplified.Builder().setName("piotr").build()
    };

//    Mockito
//        .when(client.artistAlbums(anyString(), anyInt()))
//        .thenReturn(Mono.just(paging.setItems(albums).build()))
//        .thenReturn(Mono.error(new TooManyRequestsException("Mock error", 1)))
//        .thenReturn(Mono.just(paging2.setItems(albums2).build()));
  }

  @Test
  @DisplayName("Beautiful test")
  public void test1() {
    StepVerifier.create(albumsSpotifyApi.getArtistAlbums("vasian"))
        .expectSubscription()
        .expectNextMatches(a -> a.getName().equals("vasian"))
        .expectNextMatches(a -> a.getName().equals("piotr"))
        .verifyComplete();
  }

  @Autowired
  SpotifyClient pizden;

  @Test
  @SneakyThrows
  void testHuya() {
    var spot = new SpotifyApi
        .Builder()
        .setAccessToken("BQB4s0OiZxyiiQFOfAieXWAXsyv9Uph4FsMp6GBtljxklkiW2oLx2a5bWiPgNs0_Nvipg5-Ic5BOom86_5fnkbqpwwSyc9QqWPpj1hQI8Krf_bJgRB9fR0TAX3GBy-TNbB6QC0R64P_UBzgPC_LFQ2CDwedI4FnnHinz7p6Z2tuM6nVKoGQNQ92huf_fgrmWYMrxpeZAeqqsvchGIU5tdmNUsrPapFB30L4tH3fLJWLYz5azcDK6UUgml67BBtlGeQHh0HJ07FAE5WAiLqekbA")
        .build();
    var huy = spot.getArtistsAlbums("0oSGxfWSnnOXhD2fKuz2Gy").limit(50).album_type("album").build().execute();
    var huy2 = spot.getArtistsAlbums("0oSGxfWSnnOXhD2fKuz2Gy").limit(50).offset(huy.getItems().length).album_type("album").build().execute();
    System.out.println(huy.getItems().length);
    System.out.println(huy.getNext());
    System.out.println(huy.getItems()[huy.getItems().length - 1]);
    System.out.println(huy2.getItems().length);
    System.out.println(huy2.getNext());
    System.out.println(huy2.getItems()[0]);
    System.out.println(huy2.getItems()[1]);
  }

  @Test
  @SneakyThrows
  void testZalupbI() {
    var counter = new AtomicInteger();
    Mono.zip(
        pizden.artistAlbums("0oSGxfWSnnOXhD2fKuz2Gy", 0).delayElement(Duration.ofSeconds(2)),
        Mono.just(0)
    ).delayElement(Duration.ofSeconds(2)).expand(t -> {
      var albums = t.getT1();
      var offset = t.getT2() + albums.getTotal() - 1;
      if (isEmpty(albums.getNext())) {
        return Mono.empty();
      } else {
        return Mono.zip(
            pizden.artistAlbums("0oSGxfWSnnOXhD2fKuz2Gy", offset).delayElement(Duration.ofSeconds(2)),
            Mono.just(offset)
        );
      }
    })
        .delayElements(Duration.ofSeconds(2))
        .doOnEach(v -> System.out.println(System.currentTimeMillis()))
        .subscribe();

    Thread.sleep(100000);
  }
}