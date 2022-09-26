package com.reactivespring.moviesinfoservice.controller;

import com.reactivespring.moviesinfoservice.domain.MovieInfo;
import com.reactivespring.moviesinfoservice.service.MovieInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/v1")
public class MovieInfoController {

    private final MovieInfoService movieInfoService;

    MovieInfoController(MovieInfoService movieInfoService) {
        this.movieInfoService = movieInfoService;
    }

    @ResponseStatus(CREATED)
    @PostMapping("/movieinfos")
    public Mono<MovieInfo> postMovieInfoService(@RequestBody @Valid MovieInfo movieInfo) {
        return movieInfoService.add(movieInfo);
    }

    @GetMapping("/movieinfos")
    public Flux<MovieInfo> findAll() {
        return movieInfoService.findAll().log();
    }

    @GetMapping("/movieinfos/{id}")
    public Mono<ResponseEntity<MovieInfo>> findById(@PathVariable String id) {
        return movieInfoService.findById(id)
                .map(ResponseEntity.ok()::body)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .log();
    }

    @PutMapping("/movieinfos/{id}")
    public Mono<ResponseEntity<MovieInfo>> putMovieInfoService(@PathVariable String id, @RequestBody MovieInfo movieInfo) {
        return movieInfoService.update(id, movieInfo)
                .map(ResponseEntity.ok()::body)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .log();
    }

    @DeleteMapping("/movieinfos/{id}")
    @ResponseStatus(NO_CONTENT)
    public Mono<Void> deleteById(@PathVariable String id) {
        return movieInfoService.deleteById(id).log();
    }
}
