package com.reactivespring.moviesinfoservice.controller;

import com.reactivespring.moviesinfoservice.domain.MovieInfo;
import com.reactivespring.moviesinfoservice.repository.MovieInfoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class MovieInfoControllerTest {

    @Autowired
    private MovieInfoRepository movieInfoRepository;

    @Autowired
    WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        var movieinfos = List.of(new MovieInfo(null, "Batman Begins",
                        2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
                new MovieInfo(null, "The Dark Knight",
                        2008, List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18")),
                new MovieInfo("abc", "Dark Knight Rises",
                        2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20")));

        movieInfoRepository
                .deleteAll()
                .thenMany(movieInfoRepository.saveAll(movieinfos))
                .blockLast();
    }

    private final static String MOVIES_INFO_URL = "/v1/movieinfos";

    @AfterEach
    void tearDown() {
        movieInfoRepository.deleteAll().block();
    }

    @Test
    void postMovieInfoService() {

        var movieInfo = new MovieInfo(null, "Batman Begins1",
                2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));

        webTestClient.post()
                .uri(MOVIES_INFO_URL)
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(MovieInfo.class)
                .consumeWith(result -> {
                    var newMovieInfo = result.getResponseBody();
                    assert newMovieInfo != null;
                    assert newMovieInfo.getMovieInfoId() != null;
                });
    }

    @Test
    void findAll() {
        webTestClient
                .get()
                .uri(MOVIES_INFO_URL)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(MovieInfo.class)
                .hasSize(3);
    }

    @Test
    void findById() {
        webTestClient
                .get()
                .uri(MOVIES_INFO_URL + "/abc")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(MovieInfo.class)
                .consumeWith(result -> {
                    var newMovieInfo = result.getResponseBody();
                    assert newMovieInfo != null;
                    assert newMovieInfo.getMovieInfoId().equals("abc");
                    assert newMovieInfo.getName().equals("Dark Knight Rises");
                });
    }

    @Test
    void findById_notFound() {
        webTestClient
                .get()
                .uri(MOVIES_INFO_URL + "/cde")
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void putMovieInfoService() {
        String title = "teste put endpoint";
        var movieInfo = new MovieInfo("abc", title,
                2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));

        webTestClient.put()
                .uri(MOVIES_INFO_URL + "/abc")
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(MovieInfo.class)
                .consumeWith(result -> {
                    var newMovieInfo = result.getResponseBody();
                    assert newMovieInfo != null;
                    assert newMovieInfo.getMovieInfoId().equals("abc");
                    assert title.equals(newMovieInfo.getName());
                });
    }

    @Test
    void putMovieInfoService_notFound() {
        var id = "edf";
        String title = "teste put endpoint";
        var movieInfo = new MovieInfo(id, title,
                2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));
        webTestClient
                .put()
                .uri(MOVIES_INFO_URL + "/{id}", id)
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void deleteById() {
        webTestClient
                .delete()
                .uri(MOVIES_INFO_URL + "/abc")
                .exchange()
                .expectStatus()
                .isNoContent();
    }
}