# innoq-blockchain-kotlin-spring-webflux

This is a proof-of-concept for a simple Blockchain implementation in Kotlin and using Spring WebFlux in Spring Boot for its HTTP interface.

It has not really been optimized for speed, but we tried to explorer Kotlin a bit, play around with some idioms like `let`, `with` and the like, try to forget Java-syntax for constructors and such. 

Mining has been done in a reactive way to get a grip on Flux and Mono, both part of Project Reactor, one building block of Spring WebFlux.
