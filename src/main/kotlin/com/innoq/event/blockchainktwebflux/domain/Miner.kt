package com.innoq.event.blockchainktwebflux.domain

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.SynchronousSink
import java.util.concurrent.Callable
import java.util.concurrent.atomic.AtomicLong
import java.util.function.BiFunction

class Miner {
    fun nextBlock(chain: BlockChain, timestamp: Long = System.currentTimeMillis()): Mono<Block> {
        val proofGenerator = Flux.generate<Long, AtomicLong>(Callable { AtomicLong() }, BiFunction { state, sink ->
            sink.next(state.getAndIncrement())
            state
        })

        val candidateGenerator = chain.latestBlock()
                .map { it.newCandidate(timestamp) }
                .flatMapMany({ b -> Flux.generate<Block> { it.next(b) } })

        return candidateGenerator
                .zipWith(proofGenerator)
                .map { it.t1.copy(proof = it.t2) }
                .skipUntil { it.isValid() }
                .next()
    }
}