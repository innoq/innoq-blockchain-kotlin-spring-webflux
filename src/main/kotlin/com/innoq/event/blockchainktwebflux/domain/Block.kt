package com.innoq.event.blockchainktwebflux.domain

import com.fasterxml.jackson.databind.ObjectMapper
import java.security.MessageDigest

data class Block(val index: Long, val timestamp: Long, val proof: Long, val transactions: List<Transaction>, val previousBlockHash: String) {

    companion object {
        val objectMapper = ObjectMapper()
        init {
            objectMapper.registerModule(JacksonBlockModule())
        }
    }

    fun hash(): String = objectMapper.writeValueAsBytes(this).let { MessageDigest.getInstance("SHA-256").digest(it) }.let { encode(it) }

    fun isValid() = hash().startsWith("0000")

    fun newCandidate(newTimestamp: Long, proof: Long): Block = this.copy(
            index = this.index + 1,
            timestamp = newTimestamp,
            transactions = emptyList(),
            previousBlockHash = this.hash(),
            proof = proof
    )

    private fun encode(bytes: ByteArray): String {

        val rv = StringBuilder()
        for (i in 0 until bytes.size) {
            rv.append(Integer.toHexString((bytes.get(i).toInt() and 0xff) + 0x100).substring(1))
        }
        return rv.toString()

    }
}