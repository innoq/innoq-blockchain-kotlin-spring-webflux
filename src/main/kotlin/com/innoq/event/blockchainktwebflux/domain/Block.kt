package com.innoq.event.blockchainktwebflux.domain

import com.fasterxml.jackson.databind.ObjectMapper
import java.security.MessageDigest

data class Block(val index: Long, val timestamp: Long, val proof: Long, val transactions: List<Transaction>, val previousBlockHash: String) {

    private fun encode(bytes: ByteArray): String = bytes
            .map { Integer.toHexString((it.toInt() and 0xff) + 0x100).substring(1) }
            .reduce { a, b -> a + b }

    internal fun hash(): String = objectMapper
            .writeValueAsBytes(this)
            .let { MessageDigest.getInstance("SHA-256").digest(it) }
            .let { encode(it) }

    /**
     * A block is valid if its hash starts with n-zeros
     */
    fun isValid() = hash().startsWith("0000")

    /**
     * Creates a new candidate from this block with based on a new timestampe and
     * proposed proof.
     */
    fun newCandidate(newTimestamp: Long, proof: Long, transactions: List<Transaction> = emptyList()) = this
            .copy(
                    index = this.index + 1,
                    timestamp = newTimestamp,
                    transactions = transactions,
                    previousBlockHash = this.hash(),
                    proof = proof
            )

    companion object {
        val objectMapper = ObjectMapper()

        init {
            objectMapper.registerModule(JacksonBlockModule())
        }
    }
}