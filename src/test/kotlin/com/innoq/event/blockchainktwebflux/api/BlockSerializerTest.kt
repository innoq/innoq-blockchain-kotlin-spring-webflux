package com.innoq.event.blockchainktwebflux.api

import com.innoq.event.blockchainktwebflux.domain.Block
import com.innoq.event.blockchainktwebflux.domain.Transaction
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@JsonTest
class BlockSerializerTest {
    @Autowired
    lateinit var jacksonTester: JacksonTester<Block>

    @Test
    fun serializerShouldWork(): Unit {
        val expected = """
            {"index":1,"timestamp":0,"proof":1917336,"transactions":[{"id":"b3c973e2-db05-4eb5-9668-3e81c7389a6d","timestamp":0,"payload":"I am Heribert Innoq"}],"previousBlockHash":"0"}
        """.trimIndent()

        val block = Block(1, 0, 1917336, arrayListOf(Transaction("b3c973e2-db05-4eb5-9668-3e81c7389a6d", 0, "I am Heribert Innoq")), "0")

        assertThat(jacksonTester.write(block))
                .isEqualToJson(expected)
    }
}