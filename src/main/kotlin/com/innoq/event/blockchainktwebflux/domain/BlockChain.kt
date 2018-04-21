package com.innoq.event.blockchainktwebflux.domain

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import reactor.core.scheduler.Schedulers
import java.time.Clock
import java.util.*
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.atomic.AtomicLong
import java.util.stream.Stream

class BlockChain(initialBlocks: List<Block>, private val eventPublisher: EventPublisher, private val clock: Clock, private val validBlockPrefix: String = "0000") {

    private val blocks = mutableListOf(*initialBlocks.toTypedArray())
    private val lastIndex = AtomicLong(initialBlocks.last().index)
    private val pendingTransactions: Queue<Transaction> = LinkedBlockingDeque()

    val blockHeight: Int
        get() = this.blocks.size

    fun mine() = computeNextBlock().doOnNext { blocks.add(it) }

    fun queue(payload: Payload) = Mono.fromSupplier {
        val pendingTransaction = Transaction(UUID.randomUUID().toString(), clock.millis(), payload)
        pendingTransactions.add(pendingTransaction)
        eventPublisher.publishNewTransaction(pendingTransaction)
        pendingTransaction
    }

    fun findTransaction(transactionId: String): Mono<Transaction> =
            Mono.justOrEmpty(blocks.flatMap { it.transactions }.firstOrNull { it.id == transactionId }
                    ?: pendingTransactions.firstOrNull { it.id == transactionId })

    private fun computeNextBlock(): Mono<Block> {
        val template = blocks.last().copy(
                index = lastIndex.incrementAndGet(),
                timestamp = clock.millis(),
                transactions = selectTransactions(5))

        // @formatter:off
        return Flux.fromStream(Stream.iterate(0L, Long::inc))
                .parallel().runOn(Schedulers.parallel())
                    .map { template.copy(proof = it) }
                    .filter{ it.hash().startsWith(validBlockPrefix) }
                .sequential()
                .next()
        // @formatter:on
    }

    private fun selectTransactions(maxNumberOfTransactions: Int): List<Transaction> =
            1.rangeTo(maxNumberOfTransactions).map { pendingTransactions.poll() }
                    .filter { it != null }.toList()


}
