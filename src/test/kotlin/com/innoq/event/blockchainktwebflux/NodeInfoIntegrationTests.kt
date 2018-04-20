package com.innoq.event.blockchainktwebflux

import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.Matchers.isEmptyOrNullString
import org.hamcrest.Matchers.not
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NodeInfoIntegrationTests {

    @Autowired
    lateinit var webClient: WebTestClient

    @Test
    fun Request_on_Root_URL_returns_NodeInfo() {
        webClient.get().uri("/").exchange()
                .expectStatus().isOk
                .expectBody().returnResult().apply {
                    val json = ObjectMapper().readTree(responseBody)
                    assertThat(json.at("/id").textValue(), not(isEmptyOrNullString()))
                }
    }
}
