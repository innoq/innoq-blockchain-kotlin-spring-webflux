package com.innoq.event.blockchainktwebflux.domain

class Chain(genesisBlock: Block) {
    val blocks: List<Block>

    init {
        blocks = listOf(genesisBlock)
    }
}