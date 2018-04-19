package com.innoq.event.blockchainktwebflux.domain

import reactor.core.publisher.Mono

class BlockChain(genesisBlock: Block) {

    val blocks: MutableList<Block>

    init {
        blocks = mutableListOf(genesisBlock)
    }

    val blockHeight: Int
        get() = this.blocks.size

    fun latestBlock(): Mono<Block> {
        return Mono.just(blocks.last())
    }

    fun add(nextBlock: Block) {
        blocks.add(nextBlock)
    }

}

