package com.innoq.event.blockchainktwebflux.domain

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.SynchronousSink
import java.util.concurrent.Callable
import java.util.function.BiFunction

class Miner {

    fun nextBlock(blockChain: BlockChain, timestamp: Long): Mono<Block> {
        return Flux.from(blockChain.latestBlock())
                .flatMap { latestBlock -> Flux.generate(stateSupplier(), blockCandidateGenerator(latestBlock, timestamp)) }
                .skipUntil { blockCandidate -> blockCandidate.isValid() }
                .next()
    }

    private fun stateSupplier() = Callable<Long> { 0 }

    private fun blockCandidateGenerator(latestBlock: Block, timestamp: Long): BiFunction<Long, SynchronousSink<Block>, Long> {
        return BiFunction { state, sink ->
            val blockCandidate = latestBlock.copy(
                    index = latestBlock.index + 1,
                    timestamp = timestamp,
                    proof = state,
                    transactions = emptyList(),
                    previousBlockHash = latestBlock.hash()
            )
            sink.next(blockCandidate)

            state.plus(1L)
        }
    }

}