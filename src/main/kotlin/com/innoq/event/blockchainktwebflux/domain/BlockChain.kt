package com.innoq.event.blockchainktwebflux.domain

import reactor.core.publisher.Flux

class BlockChain(genesisBlock: Block) {

    val blocks: MutableList<Block> = mutableListOf(genesisBlock)

    val blockHeight: Int
        get() = this.blocks.size

    fun mine() =
        Flux.generate<Block>{ sink -> sink.next(computeNextBlock()) }.doOnNext {blocks.add(it)}

    internal fun computeNextBlock(timestamp: Long = System.currentTimeMillis()) =
        generateSequence(0L) { it + 1 }
                .map { blocks.last().newCandidate(timestamp, it) }
                .dropWhile { it.isValid().not() }
                .first()
}

