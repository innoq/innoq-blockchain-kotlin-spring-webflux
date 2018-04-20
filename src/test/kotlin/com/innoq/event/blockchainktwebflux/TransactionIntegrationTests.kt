package com.innoq.event.blockchainktwebflux

import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.greaterThan
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integrationtest")
class TransactionIntegrationTests {

    @Autowired
    lateinit var webClient: WebTestClient

    @Test
    fun returns_pending_transaction() {
        var location = ""
        webClient.post().uri("/transactions")
                .syncBody("""{"payload": "foo"}""")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isCreated
                .expectBody().returnResult().apply {
                    val json = ObjectMapper().readTree(responseBody)
                    assertThat(json.at("/payload").textValue(), equalTo("foo"))
                    location = responseHeaders[HttpHeaders.LOCATION]!!.first()
                }

        webClient.get().uri(location).exchange()
                .expectStatus().isOk
                .expectBody().returnResult().apply {
                    val json = ObjectMapper().readTree(responseBody)
                    assertThat(json.at("/payload").textValue(), equalTo("foo"))
                }
    }

    @Test
    fun returns_confirmed_transaction() {
        var location = ""
        webClient.post().uri("/transactions")
                .syncBody("""{"payload": "foo"}""")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isCreated
                .expectBody().returnResult().apply {
                    val json = ObjectMapper().readTree(responseBody)
                    assertThat(json.at("/payload").textValue(), equalTo("foo"))
                    location = responseHeaders[HttpHeaders.LOCATION]!!.first()
                }

        webClient.get().uri("/mine")
                .exchange()
                .expectStatus().isOk
                .expectBody().returnResult().apply {
                    assertThat(responseBody!!.size, greaterThan(0))
                }

        webClient.get().uri(location).exchange()
                .expectStatus().isOk
                .expectBody().returnResult().apply {
                    val json = ObjectMapper().readTree(responseBody)
                    assertThat(json.at("/payload").textValue(), equalTo("foo"))
                }
    }

    @Test
    fun returns_notfound_for_unknown_transaction() {
        webClient.get().uri("/transactions/0815").exchange()
                .expectStatus().isNotFound
    }
}
