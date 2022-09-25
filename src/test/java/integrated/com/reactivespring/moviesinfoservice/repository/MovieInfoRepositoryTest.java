package com.reactivespring.moviesinfoservice.repository;

import com.reactivespring.moviesinfoservice.domain.MovieInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
@ActiveProfiles("test")
class MovieInfoRepositoryTest {

    @Autowired
    MovieInfoRepository movieInfoRepository;

    @BeforeEach
    public void setup() {
        var moviesInfo = List.of(
                new MovieInfo(null, "Batman Begins",
                        2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
                new MovieInfo("b", "The Dark Knight",
                        2008, List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-06-15"))
        );
        movieInfoRepository.saveAll(moviesInfo).blockLast();
    }

    @Test
    void findAll() {
        var moviesInfo = movieInfoRepository.findAll().log();
        StepVerifier.create(moviesInfo).expectNextCount(2).verifyComplete();
    }

    @Test
    void findById() {
        var movieInfo = movieInfoRepository.findById("b");
        StepVerifier.create(movieInfo)
                .assertNext(info -> {
                    assertEquals("The Dark Knight", info.getName());
                });
    }

    @Test
    void save() {
        var movieInfo = movieInfoRepository.save(new MovieInfo("b", "The Dark Knight1",
                2008, List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-06-15"))).log();
        StepVerifier.create(movieInfo)
                .assertNext(info -> {
                    assertEquals("The Dark Knight1", info.getName());
                });
    }
}