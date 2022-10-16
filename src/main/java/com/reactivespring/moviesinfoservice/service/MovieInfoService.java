package com.reactivespring.moviesinfoservice.service;

import com.reactivespring.moviesinfoservice.domain.MovieInfo;
import com.reactivespring.moviesinfoservice.repository.MovieInfoRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MovieInfoService {

    private final MovieInfoRepository movieInfoRepository;

    MovieInfoService(MovieInfoRepository movieInfoRepository) {
        this.movieInfoRepository = movieInfoRepository;
    }

    public Mono<MovieInfo> add(MovieInfo movieInfo) {
        return movieInfoRepository.insert(movieInfo);
    }

    public Flux<MovieInfo> findAll() {
        return movieInfoRepository.findAll();
    }

    public Mono<MovieInfo> findById(String id) {
        return movieInfoRepository.findById(id);
    }

    public Mono<Void> deleteById(String id) {
        return movieInfoRepository.deleteById(id);
    }

    public Mono<MovieInfo> update(String id, MovieInfo movieInfo) {
        return movieInfoRepository.findById(id)
                .flatMap(movie -> {
                    movie.setName(movieInfo.getName());
                    movie.setCast(movieInfo.getCast());
                    movie.setReleaseDate(movie.getReleaseDate());
                    movie.setYear(movie.getYear());
                    return movieInfoRepository.save(movie);
                });
    }

    public Flux<MovieInfo> findByYear(Integer year) {
        return movieInfoRepository.findByYear(year);
    }
}
