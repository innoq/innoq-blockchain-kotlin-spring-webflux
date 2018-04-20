package com.innoq.event.blockchainktwebflux.domain

import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import java.time.Clock
import kotlin.test.assertEquals
import kotlin.test.assertNull


class BlockChainTest {

    @Test
    fun looks_up_confirmed_transaction_with_id() {
        // given
        val transactionId = "id"

        val eventPublisher = mockk<EventPublisher>()
        val clock = mockk<Clock>()
        val block1 = mockk<Block>()
        every { block1.index } returns 1
        every { block1.transactions } returns emptyList()

        val block2 = mockk<Block>()
        val transaction1 = mockk<Transaction>()
        every { transaction1.id } returns "other"
        val transaction2 = mockk<Transaction>()
        every { transaction2.id } returns transactionId
        every { block2.index } returns 2
        every { block2.transactions } returns listOf(transaction1, transaction2)

        val blockChain = BlockChain(listOf(block1, block2), eventPublisher, clock)

        // when
        val returnedTransaction = blockChain.findTransaction(transactionId).block()

        // then
        assertEquals(transaction2, returnedTransaction)
    }

    @Test
    fun looks_up_pending_transaction_with_id() {
        // given
        val eventPublisher = mockk<EventPublisher>(relaxed = true)
        val block1 = mockk<Block>()
        every { block1.index } returns 1
        every { block1.transactions } returns emptyList()
        val blockChain = BlockChain(listOf(block1), eventPublisher, Clock.systemDefaultZone())

        blockChain.queue(Payload("other"))
        val transaction = blockChain.queue(Payload("wanted")).block()!!

        // when
        val returnedTransaction = blockChain.findTransaction(transaction.id).block()

        // then
        assertEquals(transaction, returnedTransaction)
    }

    @Test
    fun returns_null_if_transaction_with_id_is_not_found() {
        // given
        val transactionId = "id"

        val eventPublisher = mockk<EventPublisher>()
        val clock = mockk<Clock>()
        val block1 = mockk<Block>()
        every { block1.index } returns 1
        every { block1.transactions } returns emptyList()

        val block2 = mockk<Block>()
        val transaction = mockk<Transaction>()
        every { transaction.id } returns "other"
        every { block2.index } returns 2
        every { block2.transactions } returns listOf(transaction)
        val blockChain = BlockChain(listOf(block1, block2), eventPublisher, clock)

        // when
        val returnedTransaction = blockChain.findTransaction(transactionId)

        // then
        assertNull(returnedTransaction)
    }
}