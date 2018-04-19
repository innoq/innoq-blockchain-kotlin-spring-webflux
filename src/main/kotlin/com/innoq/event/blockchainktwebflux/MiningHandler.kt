package com.innoq.event.blockchainktwebflux

import com.innoq.event.blockchainktwebflux.domain.BlockChain
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Mono

class MiningHandler(val chain: BlockChain) {

    fun mine(request: ServerRequest): Mono<ServerResponse> {
        return ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(chain.mine())
    }

}
