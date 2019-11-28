package argoko.composefutures

import java.util.concurrent.CompletableFuture

fun main() {
    val x = CompletableFuture.supplyAsync { 10 }
            .thenAccept {
                log.info("Composing")
                throw IllegalStateException("Woot")
            }
            .exceptionally { log.info("exception $it"); null }

    val y = CompletableFuture.supplyAsync { 10 }
            .thenAccept {
                log.info("Composing")
                throw IllegalStateException("Woot")
            }
            .thenApply { x -> x }
            .exceptionally { log.info("exception $it"); null }

    listOf(x,y).forEach { it.join() }
}