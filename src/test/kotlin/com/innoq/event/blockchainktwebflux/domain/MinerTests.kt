package com.innoq.event.blockchainktwebflux.domain

import com.innoq.event.blockchainktwebflux.genesisBlock
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.util.stream.IntStream.rangeClosed
import kotlin.test.junit.JUnitAsserter.assertEquals
import kotlin.test.junit.JUnitAsserter.assertTrue

class MinerTests {

    @Test
    fun nextBlock_blockChainWithGenesisBlockOnly_returnsNextBlock() {
        // arrange
        val blockChain = BlockChain(genesisBlock(), fixedClock(1234))

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
        assertTrue("Next block is not valid", nextBlock!!.isValid())
    }

    @Test
    fun nextBlock_blockChainWithGenesisBlockAndLessThanFivePendingTransactions_returnsNextBlockWithAllPendingTransactions() {
        // arrange
        val blockChain = BlockChain(genesisBlock(), fixedClock(1234))

        rangeClosed(1, 3).forEach { index ->
            blockChain.queue(Payload("new transaction $index"))
        }

        // act
        val nextBlock = blockChain.mine().block()

        // assert
        assertEquals("Next block doesn't contain all pending transactions", nextBlock!!.transactions.size, 3)

    }

    @Test
    fun nextBlock_blockChainWithGenesisBlockAndMoreThanFivePendingTransactions_returnsNextBlockWithFivePendingTransactions() {
        // arrange
        val blockChain = BlockChain(genesisBlock(), fixedClock(1234))

        rangeClosed(1, 6).forEach { index ->
            blockChain.queue(Payload("new transaction $index"))
        }

        // act
        val nextBlock = blockChain.mine().block()

        // assert
        assertEquals("Next block doesn't contain all pending transactions", nextBlock!!.transactions.size, 5)
        assertEquals("Incorrect number of pending transactions after mining next block", blockChain.pendingTransactions().size, 1)
        assertEquals("Pending transaction is not last one added", blockChain.pendingTransactions().first().payload, Payload("new transaction 6"))
    }

    private fun fixedClock(now: Long) = Clock.fixed(Instant.ofEpochMilli(now), ZoneId.of("UTC"))

}
