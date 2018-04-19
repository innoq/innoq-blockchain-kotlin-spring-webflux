package com.innoq.event.blockchainktwebflux

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
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

@SpringBootApplication
class BlockchainKtWebfluxApplication

fun main(args: Array<String>) {
    runApplication<BlockchainKtWebfluxApplication>(*args)
}
