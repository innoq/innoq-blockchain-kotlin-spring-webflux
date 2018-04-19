package com.innoq.event.blockchainktwebflux

import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Mono

class NodeInfoHandler(val nodeInfo: NodeInfo) {

    fun index(request: ServerRequest): Mono<ServerResponse>
            = ServerResponse.ok().body(Mono.just(nodeInfo))
}