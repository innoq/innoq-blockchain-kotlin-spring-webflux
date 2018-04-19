package com.innoq.event.blockchainktwebflux

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.support.beans
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.router
import java.util.*

data class NodeInfo(val id: String = UUID.randomUUID().toString())


class Router(
        val nodeInfoHandler: NodeInfoHandler,
        val blocksHandler: BlocksHandler,
        val miningHandler: MiningHandler
) {
    fun router() = router() {
        accept(MediaType.APPLICATION_JSON).nest {
                GET("/", nodeInfoHandler::index)
                GET("/blocks", blocksHandler::showBlocks)
                GET("/mine", miningHandler::mine)
        }
    }

}

@SpringBootApplication
class BlockchainKtWebfluxApplication

fun beans() = beans {
    bean<NodeInfoHandler>()

    bean {
        Router(ref(), ref(), ref()).router()
    }

    bean<NodeInfo>()
}

fun main(args: Array<String>) {
    SpringApplicationBuilder()
            .sources(BlockchainKtWebfluxApplication::class.java)
            .initializers(beans())
            .run(*args)
}