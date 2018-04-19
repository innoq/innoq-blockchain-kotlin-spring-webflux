package com.innoq.event.blockchainktwebflux.domain

import com.fasterxml.jackson.databind.ObjectMapper
import java.security.MessageDigest

data class Block(val index: Long, val timestamp: Long, val proof: Long, val transactions: List<Transaction>, val previousBlockHash: String) {

    companion object {
        val objectMapper = ObjectMapper()
        val messageDigest = MessageDigest.getInstance("SHA-256")!!

        init {
            objectMapper.registerModule(JacksonBlockModule())
        }
    }

    fun hash(): String = objectMapper.writeValueAsBytes(this).let { messageDigest.digest(it) }.let { encode(it) }

    private fun encode(bytes: ByteArray): String {

        val rv = StringBuilder()
        for (i in 0 until bytes.size) {
            rv.append(Integer.toHexString((bytes.get(i).toInt() and 0xff) + 0x100).substring(1))
        }
        return rv.toString()

    }
}