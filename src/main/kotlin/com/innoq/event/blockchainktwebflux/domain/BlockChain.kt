package com.innoq.event.blockchainktwebflux.domain

import reactor.core.publisher.Mono
import java.time.Clock
import java.util.*
import java.util.concurrent.atomic.AtomicLong

class BlockChain(initialBlocks: List<Block>, private val eventPublisher: EventPublisher, private val clock: Clock, private val validBlockPrefix: String = "0000") {

    private val blocks = mutableListOf(*initialBlocks.toTypedArray())
    private val lastIndex = AtomicLong(initialBlocks.last().index)
    private val pendingTransactions = mutableListOf<Transaction>()

    val blockHeight: Int
        get() = this.blocks.size

    fun mine() =
            Mono.just(computeNextBlock()).doOnNext { blocks.add(it) }

    fun queue(payload: Payload) = Mono.fromSupplier {
        val pendingTransaction = Transaction(UUID.randomUUID().toString(), clock.millis(), payload)
        pendingTransactions.add(pendingTransaction)
        eventPublisher.publishNewTransaction(pendingTransaction)
        pendingTransaction
    }

    fun findTransaction(transactionId: String): Mono<Transaction> =
            Mono.justOrEmpty(blocks.flatMap { it.transactions }.firstOrNull { it.id == transactionId }
                    ?: pendingTransactions.firstOrNull { it.id == transactionId })

    private fun computeNextBlock(): Block {
        val nextIndex = lastIndex.incrementAndGet()
        val timestamp = clock.millis()
        val transactions = selectTransactions(5)

        return generateSequence(0L) { it + 1 }
                .map { blocks.last().newCandidate(nextIndex, timestamp, it, transactions) }
                .dropWhile { isValid(it).not() }
                .first()
    }

    private fun selectTransactions(maxNumberOfTransactions: Int): List<Transaction> {
        val transactions = pendingTransactions.take(maxNumberOfTransactions)
        pendingTransactions.removeAll(transactions)

        return transactions
    }

    /**
     * A block is valid if its hash starts with n-zeros
     */
    private fun isValid(block: Block) = block.hash().startsWith(validBlockPrefix)
}
