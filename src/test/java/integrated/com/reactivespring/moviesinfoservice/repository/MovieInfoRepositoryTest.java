package com.reactivespring.moviesinfoservice.repository;

import com.reactivespring.moviesinfoservice.domain.MovieInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

@DataMongoTest
@ActiveProfiles("test")
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
class MovieInfoRepositoryTest {

  @Autowired
  MovieInfoRepository movieInfoRepository;

  @BeforeEach
  public void setup() {
    var moviesInfo = List.of(new MovieInfo(null, "Batman Begins",
        2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")));
    movieInfoRepository.saveAll(moviesInfo).blockLast();
  }

  @Test
  void findAll() {
    var moviesInfo = movieInfoRepository.findAll().log();
    StepVerifier.create(moviesInfo).expectNextCount(1).verifyComplete();
  }
}