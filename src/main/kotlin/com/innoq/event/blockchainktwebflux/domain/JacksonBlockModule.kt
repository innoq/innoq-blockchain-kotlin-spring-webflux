package com.innoq.event.blockchainktwebflux.domain

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule

/**
 * A simple jackson module to make sure the generated JSON has always the same structure without having
 * a string template in the block class.
 */
class JacksonBlockModule: SimpleModule() {
    init {
        addSerializer(Block::class.java, BlockSerializer())
    }
}

private class BlockSerializer : JsonSerializer<Block>() {
    override fun serialize(block: Block?, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider?) {
        if(block == null) {
            return
        }

        jsonGenerator.apply {
            writeStartObject()
            writeNumberField("index", block.index)
            writeNumberField("timestamp", block.timestamp)
            writeNumberField("proof", block.proof)
            writeObjectField("transactions", block.transactions)
            writeStringField("previousBlockHash", block.previousBlockHash)
            writeEndObject()
        }
    }
}