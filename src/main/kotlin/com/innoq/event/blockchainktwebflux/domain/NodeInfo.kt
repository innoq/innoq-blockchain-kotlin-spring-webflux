package com.innoq.event.blockchainktwebflux.domain

import java.util.*

class NodeInfo(_chain: BlockChain) {
    val id = UUID.randomUUID().toString()
    private val chain = _chain

    val currentBlockHeight: Int
        get() = this.chain.blockHeight
}