package com.innoq.event.blockchainktwebflux.domain

import com.innoq.event.blockchainktwebflux.genesisBlock
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import kotlin.test.junit.JUnitAsserter.assertEquals
import kotlin.test.junit.JUnitAsserter.assertTrue

class MinerTests {

    @Test
    fun nextBlock_blockChainWithGenesisBlockOnly_returnsNextBlock() {
        // arrange
        val blockChain = BlockChain(genesisBlock(), Clock.fixed(Instant.ofEpochMilli(1234), ZoneId.of("UTC")))

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
}
