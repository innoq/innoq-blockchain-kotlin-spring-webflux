package com.innoq.event.blockchainktwebflux.domain

import reactor.core.publisher.Mono
import java.time.Clock
import java.util.concurrent.atomic.AtomicLong

class BlockChain(genesisBlock: Block, private val clock: Clock) {

    val blocks = mutableListOf(genesisBlock)
    val lastIndex = AtomicLong(genesisBlock.index)

    val blockHeight: Int
        get() = this.blocks.size

    fun mine() =
        Mono.just(computeNextBlock()).doOnNext {blocks.add(it)}

    private fun computeNextBlock() : Block {
        val nextIndex = lastIndex.incrementAndGet()
        val timestamp = clock.millis()

        return generateSequence(0L) { it + 1 }
                .map { blocks.last().newCandidate(nextIndex, timestamp, it) }
                .dropWhile { it.isValid().not() }
                .first()
    }
}

