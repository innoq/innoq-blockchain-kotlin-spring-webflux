package com.innoq.event.blockchainktwebflux.domain

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.stream.Stream


class Miner {
    fun nextBlock(chain: BlockChain, timestamp: Long = System.currentTimeMillis()): Mono<Block> {

        val proofGenerator = Flux.fromStream(Stream.iterate(0L) { i -> i + 1 })

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