package com.innoq.event.blockchainktwebflux

import com.innoq.event.blockchainktwebflux.domain.BlockChain
import com.innoq.event.blockchainktwebflux.domain.Miner
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Mono

class MiningHandler(val miner: Miner, val chain: BlockChain) {

    fun mine(request: ServerRequest): Mono<ServerResponse> {
        val mono = miner.nextBlock(chain).doOnSuccess {
            chain.add(it)
        }.doOnError { throw RuntimeException() }
        return ok().body(mono)
    }

}
