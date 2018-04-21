package com.innoq.event.blockchainktwebflux.domain

import java.util.*

class NodeInfo(private val chain: BlockChain, val id: String = UUID.randomUUID().toString()) {

    val currentBlockHeight: Int
        get() = this.chain.blockHeight
}