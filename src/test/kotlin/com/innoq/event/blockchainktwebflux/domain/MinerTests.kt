package com.innoq.event.blockchainktwebflux.domain

import com.innoq.event.blockchainktwebflux.genesisBlock
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Test


class MinerTests {

    @Test
    fun nextBlock_blockChainWithGenesisBlockOnly_returnsNextBlock() {
        // arrange
        val blockChain = BlockChain(genesisBlock())

        // act
        val nextBlock = blockChain.computeNextBlock(1234)

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
