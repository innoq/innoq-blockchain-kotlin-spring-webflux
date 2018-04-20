package com.innoq.event.blockchainktwebflux.domain

import reactor.core.publisher.Flux
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

class BlockChain(genesisBlock: Block) {

    val blocks = mutableListOf(genesisBlock)
    val lastIndex = AtomicLong(genesisBlock.index)

    val blockHeight: Int
        get() = this.blocks.size

    fun mine() =
        Flux.generate<Block>{ sink -> sink.next(computeNextBlock()) }.doOnNext {blocks.add(it)}

    internal fun computeNextBlock(timestamp: Long = System.currentTimeMillis()) : Block {
        val nextIndex = lastIndex.incrementAndGet()
        return generateSequence(0L) { it + 1 }
                .map { blocks.last().newCandidate(nextIndex, timestamp, it) }
                .dropWhile { it.isValid().not() }
                .first()
    }
}

