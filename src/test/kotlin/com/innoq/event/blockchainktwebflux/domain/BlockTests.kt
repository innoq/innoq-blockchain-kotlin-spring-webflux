package com.innoq.event.blockchainktwebflux.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class BlockTests {

    @Test
    fun hash_genesisBlock_returnsCorrectHash() {
        // arrange
        val genesisBlock = genesisBlock()

        // act
        val hash = genesisBlock.hash()

        // assert
        assertEquals("000000b642b67d8bea7cffed1ec990719a3f7837de5ef0f8ede36537e91cdc0e", hash)
    }

}