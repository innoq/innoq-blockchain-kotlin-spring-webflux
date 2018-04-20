package com.innoq.event.blockchainktwebflux.domain

import com.innoq.event.blockchainktwebflux.genesisBlock
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.util.stream.IntStream.rangeClosed
import kotlin.test.junit.JUnitAsserter.assertEquals

class MinerTests {

    @Test
    fun nextBlock_blockChainWithGenesisBlockOnly_returnsNextBlock() {
        // arrange
        val eventPublisher = EventPublisher()
        val blockChain = BlockChain(listOf(genesisBlock()), eventPublisher, fixedClock(1234))

        // act
        val nextBlock = blockChain.mine().block()

        // assert
        assertEquals("Next block doesn't match", Block(
                index = 2,
                timestamp = 1234,
                proof = 60229,
                transactions = emptyList(),
                previousBlockHash = genesisBlock().hash()
        ), nextBlock)
    }

    @Test
    fun nextBlock_blockChainWithGenesisBlockAndLessThanFivePendingTransactions_returnsNextBlockWithAllPendingTransactions() {
        // arrange
        val eventPublisher = EventPublisher()
        val blockChain = BlockChain(listOf(genesisBlock()), eventPublisher, fixedClock(1234))

        rangeClosed(1, 3).forEach { index ->
            blockChain.queue(Payload("new transaction $index"))
        }

        // act
        val nextBlock = blockChain.mine().block()

        // assert
        assertEquals("Next block doesn't contain all pending transactions", nextBlock!!.transactions.size, 3)
    }

    @Test
    fun nextBlock_blockChainWithGenesisBlockAndMoreThanFivePendingTransactions_returnsFirstNextBlockWithFivePendingTransactionsAndSecondNextBlockWithRemainingPendingtransactions() {
        // arrange
        val eventPublisher = EventPublisher()
        val blockChain = BlockChain(listOf(genesisBlock()), eventPublisher, fixedClock(1234))

        rangeClosed(1, 6).forEach { index ->
            blockChain.queue(Payload("new transaction $index"))
        }

        // act
        val firstNextBlock = blockChain.mine().block()
        val secondNextBlock = blockChain.mine().block()

        // assert
        assertEquals("First next block doesn't contain expected number of transactions", firstNextBlock!!.transactions.size, 5)
        assertEquals("First next block doesn't contain expected transactions", Payload("new transaction 5"), transactionPayloadIn(firstNextBlock, 4))

        assertEquals("Second next block doesn't contain expected number of transactions", secondNextBlock!!.transactions.size, 1)
        assertEquals("Second next block doesn't contain expected transactions", Payload("new transaction 6"), transactionPayloadIn(secondNextBlock, 0))
    }

    private fun fixedClock(now: Long) = Clock.fixed(Instant.ofEpochMilli(now), ZoneId.of("UTC"))

    private fun transactionPayloadIn(block: Block, index: Int): Payload {
        return block.transactions.get(index).payload
    }

}
