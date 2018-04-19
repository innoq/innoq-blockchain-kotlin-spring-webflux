package com.innoq.event.blockchainktwebflux.domain

import reactor.core.publisher.Mono

class BlockChain(genesisBlock: Block) {

    val blocks: List<Block> = listOf(genesisBlock)

    fun latestBlock(): Mono<Block> {
        return Mono.just(blocks.last())
    }

}

