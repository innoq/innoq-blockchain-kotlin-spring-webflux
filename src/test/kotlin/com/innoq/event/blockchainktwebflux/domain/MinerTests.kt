package com.innoq.event.blockchainktwebflux.domain

import com.innoq.event.blockchainktwebflux.genesisBlock
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.ZoneId

class MinerTests {

    @Test
    fun nextBlock_blockChainWithGenesisBlockOnly_returnsNextBlock() {
        // arrange
        val blockChain = BlockChain(genesisBlock(), Clock.fixed(Instant.ofEpochMilli(1234), ZoneId.of("UTC")))

        // act
        val nextBlock = blockChain.mine().block()

        // assert
        assertEquals(Block(
                index = 2,
                timestamp = 1234,
                proof = 60229,
                transactions = emptyList(),
                previousBlockHash = genesisBlock().hash()
        ), nextBlock)
        println(nextBlock!!.hash())
        assertTrue(nextBlock.isValid())
    }

}
