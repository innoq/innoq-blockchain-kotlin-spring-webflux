package com.innoq.event.blockchainktwebflux

import com.innoq.event.blockchainktwebflux.domain.*
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.support.beans
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono

@SpringBootApplication
class SimpleBlockChainApplication

fun genesisBlock() = Block(1, 0, 1917336, listOf(Transaction("b3c973e2-db05-4eb5-9668-3e81c7389a6d", 0, "I am Heribert Innoq")), "0")

fun beans() = beans {
    val chain = BlockChain(genesisBlock())
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
                ok().contentType(MediaType.APPLICATION_STREAM_JSON)
                        .body(chain.mine())
            })
        }
    }
}

fun main(args: Array<String>) {
    SpringApplicationBuilder()
            .sources(SimpleBlockChainApplication::class.java)
            .initializers(beans())
            .run(*args)
}