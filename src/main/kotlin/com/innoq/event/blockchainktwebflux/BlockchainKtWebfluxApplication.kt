package com.innoq.event.blockchainktwebflux

import com.innoq.event.blockchainktwebflux.domain.*
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.support.beans
import org.springframework.web.reactive.function.server.router


fun genesisBlock(): Block {
    return Block(1, 0, 1917336, listOf(Transaction("b3c973e2-db05-4eb5-9668-3e81c7389a6d", 0, "I am Heribert Innoq")), "0")
}

class Router(
        val nodeInfoHandler: NodeInfoHandler,
        val blocksHandler: BlocksHandler,
        val miningHandler: MiningHandler
) {
    fun router() = router {
        GET("/", nodeInfoHandler::index)
        GET("/blocks", blocksHandler::showBlocks)
        GET("/mine", miningHandler::mine)
    }

}

@SpringBootApplication
class BlockchainKtWebfluxApplication

fun beans() = beans {
    bean<NodeInfoHandler>()
    bean<BlocksHandler>()
    bean<MiningHandler>()
    bean<JacksonBlockModule>()

    bean {
        Router(ref(), ref(), ref()).router()
    }

    bean<NodeInfo>()
    bean {
        BlockChain(genesisBlock())
    }
    bean<Miner>()

}

fun main(args: Array<String>) {
    SpringApplicationBuilder()
            .sources(BlockchainKtWebfluxApplication::class.java)
            .initializers(beans())
            .run(*args)
}