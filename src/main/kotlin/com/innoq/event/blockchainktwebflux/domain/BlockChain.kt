package com.innoq.event.blockchainktwebflux.domain

import reactor.core.publisher.Mono

class BlockChain {

    fun latestBlock(): Mono<Block> {
        return Mono.just(genesisBlock())
    }

}

