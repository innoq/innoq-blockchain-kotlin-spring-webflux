package com.innoq.event.blockchainktwebflux

import com.innoq.event.blockchainktwebflux.domain.Chain
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.*
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Mono

class BlocksHandler(val chain: Chain) {

    fun showBlocks(request: ServerRequest): Mono<ServerResponse> {
        return ok().body(Mono.just(chain))
    }

}
