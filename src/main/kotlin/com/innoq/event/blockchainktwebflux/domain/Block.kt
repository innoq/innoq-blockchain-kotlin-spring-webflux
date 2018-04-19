package com.innoq.event.blockchainktwebflux.domain

import java.nio.charset.StandardCharsets.UTF_8
import java.security.MessageDigest

data class Block(val index: Long, val timestamp: Long, val proof: Long, val transactions: List<Transaction>, val previousBlockHash: String) {

    fun hash(): String {
        val messageDigest = MessageDigest.getInstance("SHA-256")
        val digest = messageDigest.digest(asJson(this).toByteArray(UTF_8))
        val hash = toHex(digest)

        return hash
    }

    private fun asJson(block: Block): String {
        return "{" +
                "\"index\":" + block.index + "," +
                "\"timestamp\":" + block.timestamp + "," +
                "\"proof\":" + block.proof+ "," +
                "\"transactions\":[" + asJson(block.transactions)+ "]," +
                "\"previousBlockHash\":\"" + block.previousBlockHash + "\"}";
    }

    private fun asJson(transactions: List<Transaction>): String {
        return transactions.map { transaction -> "{" +
                "\"id\":\"" + transaction.id + "\"," +
                "\"timestamp\":" + transaction.timestamp + "," +
                "\"payload\":\"" + transaction.payload+ "\"" +
                "}"
        }.joinToString(",")
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
