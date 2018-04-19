package com.innoq.event.blockchainktwebflux.domain

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class MinerTests {

    @Test
    fun nextBlock_blockChainWithGenesisBlockOnly_returnsNextBlock() {
        // arrange
        val miner = Miner()
        val blockChain = BlockChain()

        // act
        val nextBlock = miner.nextBlock(blockChain, 1234).block()

        // assert
        assertEquals(Block(
                index = 2,
                timestamp = 1234,
                proof = 4539,
                transactions = emptyList(),
                previousBlockHash = genesisBlock().hash()
        ), nextBlock)
        assertTrue(nextBlock!!.hash().startsWith("000"))
    }

}
