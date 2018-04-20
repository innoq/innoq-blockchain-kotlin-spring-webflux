package com.innoq.event.blockchainktwebflux

import com.fasterxml.jackson.databind.ObjectMapper
import com.innoq.event.blockchainktwebflux.domain.Event
import org.hamcrest.Matchers.*
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.APPLICATION_STREAM_JSON
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.body
import reactor.core.publisher.Mono
import kotlin.reflect.full.cast

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NodeInfoIntegrationTests {

    @Autowired
    lateinit var webClient: WebTestClient

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun Request_on_Root_URL_returns_NodeInfo() {
        webClient.get().uri("/").exchange()
                .expectStatus().isOk
                .expectBody().returnResult().apply {
                    val json = ObjectMapper().readTree(responseBody)
                    assertThat(json.at("/id").textValue(), not(isEmptyOrNullString()))
                }
    }

    @Test
    fun returns_same_id_on_each_request() {
        var idReturnedOn1stRequest: String = ""
        webClient.get().uri("/").exchange().expectBody().returnResult().apply {
            val json = objectMapper.readTree(responseBody)
            idReturnedOn1stRequest = json.at("/id").textValue()
        }
        webClient.get().uri("/").exchange()
                .expectBody().returnResult().apply {
                    val json = ObjectMapper().readTree(responseBody)
                    assertThat(json.at("/id").textValue(), equalTo(idReturnedOn1stRequest))
                }
    }

    @Test
    fun publishes_new_transactions_on_event_stream() {
        webClient.post()
                .uri("/transactions")
                .contentType(APPLICATION_JSON)
                .body(Mono.just("""
            { "payload" : "new transaction" }
            """.trimIndent())).exchange()
                .expectStatus().isCreated

        val event = webClient.get().uri("/events")
                .accept(APPLICATION_STREAM_JSON)
                .exchange()
                .returnResult(Event::class.java)
                .responseBody
                .take(1)
                .collectList()
                .block();

        assertThat(event!!.first(), not(nullValue()))
        assertThat(event.first().data, instanceOf(Map::class.java))
        assertThat(Map::class.cast(event.first().data)["payload"].toString(), equalTo("new transaction"))
    }

}
