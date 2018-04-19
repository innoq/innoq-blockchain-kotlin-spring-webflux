package com.innoq.event.blockchainktwebflux

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.support.beans
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono
import java.util.*

data class NodeInfo(val id: String = UUID.randomUUID().toString())

class NodeInfoHandler(val nodeInfo: NodeInfo) {
    fun index(request: ServerRequest): Mono<ServerResponse>
            = ok().body(Mono.just(nodeInfo))
}

class Router(val nodeInfoHandler: NodeInfoHandler) {
    fun router() = router() {
        accept(MediaType.APPLICATION_JSON).nest {
                GET("/", nodeInfoHandler::index)
        }
    }

}

@SpringBootApplication
class BlockchainKtWebfluxApplication

fun beans() = beans {
    bean<NodeInfoHandler>()

    bean {
        Router(ref()).router()
    }

    bean<NodeInfo>()
}

fun main(args: Array<String>) {
    SpringApplicationBuilder()
            .sources(BlockchainKtWebfluxApplication::class.java)
            .initializers(beans())
            .run(*args)
}