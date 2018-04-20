package com.innoq.event.blockchainktwebflux.domain

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.module.SimpleModule

/**
 * A simple jackson module to make sure the generated JSON has always the same structure without having
 * a string template in the block class.
 */
class JacksonBlockModule: SimpleModule() {
    init {
        addSerializer(Block::class.java, BlockSerializer())
        addSerializer(Transaction::class.java, TransactionSerializer())

        addDeserializer(Payload::class.java, PayloadDeserializer())
    }
}

private class BlockSerializer : JsonSerializer<Block>() {
    override fun serialize(block: Block, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider?) {
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

private class TransactionSerializer : JsonSerializer<Transaction>() {
    override fun serialize(transaction: Transaction, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider?) {
        jsonGenerator.apply {
            writeStartObject()
            writeStringField("id", transaction.id)
            writeNumberField("timestamp", transaction.timestamp)
            writeStringField("payload", transaction.payload.value)
            writeEndObject()
        }
    }
}

private class PayloadDeserializer : JsonDeserializer<Payload>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext?) =
        Payload(p.readValueAsTree<JsonNode>().get("payload").asText())
}