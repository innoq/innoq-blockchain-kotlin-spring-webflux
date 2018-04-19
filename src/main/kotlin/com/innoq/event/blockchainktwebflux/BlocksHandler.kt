package com.innoq.event.blockchainktwebflux

import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

class BlocksHandler {

    fun showBlocks(request: ServerRequest) =
            ServerResponse.ok().body(Mono.just("not yet implemented"), String::class.java)

}
