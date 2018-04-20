package com.innoq.event.blockchainktwebflux

import com.innoq.event.blockchainktwebflux.domain.*
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import org.springframework.http.MediaType.APPLICATION_STREAM_JSON
import org.springframework.web.reactive.function.server.ServerResponse.*
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.bodyToMono
import org.springframework.web.reactive.function.server.router
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono
import java.time.Clock

@SpringBootApplication
class SimpleBlockChainApplication

fun genesisBlock() = Block(1, 0, 1917336, listOf(Transaction("b3c973e2-db05-4eb5-9668-3e81c7389a6d", 0, Payload("I am Heribert Innoq"))), "0")

fun beans() = beans {
    val eventPublisher = EventPublisher()
    val chain = BlockChain(listOf(genesisBlock()), eventPublisher, Clock.systemUTC())
    val nodeInfo = NodeInfo(chain)

    bean<JacksonBlockModule>()
    bean {
        router {
            GET("/", {
                ok().body(Mono.just(nodeInfo))
            })
            GET("/blocks", {
                ok().body(Mono.just(chain))
            })
            GET("/mine", {
                ok().contentType(APPLICATION_STREAM_JSON)
                        .body(chain.mine())
            })
            POST("/transactions", { request ->
                request.bodyToMono<Payload>()
                        .flatMap { chain.queue(it) }
                        .flatMap {
                            created(UriComponentsBuilder.fromUri(request.uri())
                                    .pathSegment("{id}")
                                    .buildAndExpand(mapOf("id" to it.id)).encode().toUri())
                                    .body(Mono.just(it))
                        }
            })
            GET("/transactions/{id}", {
                chain.findTransaction(it.pathVariable("id"))
                        .flatMap { ok().body(Mono.just(it)) }
                        .switchIfEmpty(notFound().build())
            })
            GET("/events", {
                ok().contentType(APPLICATION_STREAM_JSON)
                        .body(eventPublisher.events())
            })
        }
    }
}

class BeansInitializer : ApplicationContextInitializer<GenericApplicationContext> {
    override fun initialize(context: GenericApplicationContext) =
            beans().initialize(context)

}

fun main(args: Array<String>) {
    runApplication<SimpleBlockChainApplication>(*args)
}