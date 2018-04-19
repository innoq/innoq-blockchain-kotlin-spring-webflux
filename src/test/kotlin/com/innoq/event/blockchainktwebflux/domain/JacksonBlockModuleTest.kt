package com.innoq.event.blockchainktwebflux.domain

import com.innoq.event.blockchainktwebflux.genesisBlock
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@JsonTest
class JacksonBlockModuleTest {
    @Autowired
    lateinit var jacksonTester: JacksonTester<Block>

    @Test
    fun serializerShouldWork(): Unit {
        val expected = """
            {"index":1,"timestamp":0,"proof":1917336,"transactions":[{"id":"b3c973e2-db05-4eb5-9668-3e81c7389a6d","timestamp":0,"payload":"I am Heribert Innoq"}],"previousBlockHash":"0"}
        """.trimIndent()

        assertThat(jacksonTester.write(genesisBlock()))
                .isEqualToJson(expected)
    }
}