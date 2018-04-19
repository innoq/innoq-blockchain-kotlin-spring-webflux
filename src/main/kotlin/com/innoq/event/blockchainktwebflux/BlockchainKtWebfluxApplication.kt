package com.innoq.event.blockchainktwebflux

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.runApplication
import org.springframework.context.support.beans
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.RequestPredicates.GET
import org.springframework.web.reactive.function.server.RouterFunctions.route
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

data class NodeInfo(val id: String)

@RestController
class StandardRestController {
    @GetMapping("/standardRestIndex")
    fun index(): NodeInfo = NodeInfo("4711")
}

@RestController
class WebfluxRestController {
    @GetMapping(value = "/webfluxRestIndex", produces = arrayOf(MediaType.APPLICATION_STREAM_JSON_VALUE))
    fun index(): Mono<NodeInfo> = Mono.just(NodeInfo("4711"))
}

class FunctionalHandler {
    fun index(request: ServerRequest): Mono<ServerResponse>
            = ok().body(Mono.just(NodeInfo("4711")))
}

class Router(val functionalHandler: FunctionalHandler) {
    fun router() = router() {
        accept(MediaType.APPLICATION_JSON).nest {
                GET("/", functionalHandler::index)
        }
    }

}

@SpringBootApplication
class BlockchainKtWebfluxApplication

fun beans() = beans {
    bean<FunctionalHandler>()

    bean {
        Router(ref()).router()
    }
}

fun main(args: Array<String>) {
    SpringApplicationBuilder()
            .sources(BlockchainKtWebfluxApplication::class.java)
            .initializers(beans())
            .run(*args);
}