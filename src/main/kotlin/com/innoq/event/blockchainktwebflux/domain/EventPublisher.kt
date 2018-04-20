package com.innoq.event.blockchainktwebflux.domain

import reactor.core.publisher.Flux
import reactor.core.publisher.ReplayProcessor
import java.util.concurrent.atomic.AtomicLong

class EventPublisher {

    private val nextEventId = AtomicLong()
    private val events: ReplayProcessor<Event> = ReplayProcessor.create();

    fun publishNewTransaction(transaction: Transaction) {
        events.onNext(Event(nextEventId.incrementAndGet(), "new_transaction", transaction))
    }

    fun events(): Flux<Event> {
        return events.publish().autoConnect()
    }

}

data class Event(val id: Long, val event: String, val data: Any)

