package com.innoq.event.blockchainktwebflux.domain

import com.fasterxml.jackson.databind.ObjectMapper
import java.security.MessageDigest

data class Block(val index: Long, val timestamp: Long, val proof: Long, val transactions: List<Transaction>, val previousBlockHash: String) {

    private fun encode(bytes: ByteArray): String = bytes.fold("", { str, it -> str + "%02x".format(it) })

    fun hash() = objectMapper
            .writeValueAsBytes(this)
            .let { MessageDigest.getInstance("SHA-256").digest(it) }
            .let { encode(it) }

    companion object {
        val objectMapper = ObjectMapper()

        init {
            objectMapper.registerModule(JacksonBlockModule())
        }
    }
}