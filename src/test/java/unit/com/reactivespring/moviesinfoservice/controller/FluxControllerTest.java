package com.reactivespring.moviesinfoservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;


@AutoConfigureWebClient
@WebFluxTest(controllers = FluxController.class)
public class FluxControllerTest {

  @Autowired
  WebTestClient client;

  @Test
  void flux() {
    client
        .get()
        .uri("/flux")
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectBodyList(Integer.class)
        .hasSize(3);
  }

  @Test
  void flux_return_body() {
    var flux = client
        .get()
        .uri("/flux")
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .returnResult(Integer.class)
        .getResponseBody();

    StepVerifier.create(flux)
        .expectNext(1, 2, 3)
        .verifyComplete();
  }
}