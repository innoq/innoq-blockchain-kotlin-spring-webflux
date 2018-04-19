package com.innoq.event.blockchainktwebflux

import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Mono

class MiningHandler {

    fun mine(request: ServerRequest) =
            ok().body(Mono.just("not yet implemented"), String::class.java)

}
