package com.innoq.event.blockchainktwebflux.domain

import java.nio.charset.StandardCharsets.UTF_8
import java.security.MessageDigest

data class Block(val index: Long, val timestamp: Long, val proof: Long, val transactions: List<Transaction>, val previousBlockHash: String) {

    fun hash(): String {
        val messageDigest = MessageDigest.getInstance("SHA-256")
        val digest = messageDigest.digest(asJson().toByteArray(UTF_8))
        val hash = toHex(digest)

        return hash
    }

    private fun asJson(): String {
        // TODO implement based on block state
        return "{\"index\":1,\"timestamp\":0,\"proof\":1917336,\"transactions\":[{\"id\":\"b3c973e2-db05-4eb5-9668-3e81c7389a6d\",\"timestamp\":0,\"payload\":\"I am Heribert Innoq\"}],\"previousBlockHash\":\"0\"}";
    }

    private fun toHex(bytes: ByteArray): String {
        val HEX_CHARS = "0123456789abcdef"
        val result = StringBuilder(bytes.size * 2)

        bytes.forEach {
            val i = it.toInt()
            result.append(HEX_CHARS[i shr 4 and 0x0f])
            result.append(HEX_CHARS[i and 0x0f])
        }

        return result.toString()
    }

}

fun genesisBlock(): Block {
    return Block(1, 0, 1917336, listOf(Transaction("b3c973e2-db05-4eb5-9668-3e81c7389a6d", 0, "I am Heribert Innoq")), "0")
}
