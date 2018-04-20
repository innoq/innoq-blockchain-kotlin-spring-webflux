package com.innoq.event.blockchainktwebflux.domain

data class Payload(val value: String)

data class Transaction(val id: String, val timestamp: Long, val payload: Payload)
