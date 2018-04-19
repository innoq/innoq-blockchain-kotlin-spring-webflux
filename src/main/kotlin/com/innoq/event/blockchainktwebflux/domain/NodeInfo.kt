package com.innoq.event.blockchainktwebflux.domain

import java.util.*

class NodeInfo(_chain: BlockChain) {
    val id: String
    private val chain: BlockChain

    init {
        id = UUID.randomUUID().toString()
        chain = _chain
    }

    val currentBlockHeight: Int
        get() = this.chain.blockHeight
}