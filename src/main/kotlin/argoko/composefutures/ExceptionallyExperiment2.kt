package argoko.composefutures

import argoko.extensions.KCompletableFuture
import argoko.extensions.schedule
import argoko.extensions.seconds
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.function.Consumer
import java.util.function.Function as JavaFunction


fun main() {
    val executor = Executors.newSingleThreadScheduledExecutor()

    fun <T, U> CompletableFuture<T>.thenComposeAsync(executor: Executor
                                                     , mapping: (T) -> CompletionStage<U>) : CompletableFuture<U> {
        return thenComposeAsync( JavaFunction  { it  -> mapping(it) }, executor)
    }

    fun <T, U> CompletableFuture<T>.thenApplyAsync(executor: Executor
                                                     , mapping: (T) -> U) : CompletableFuture<U> {
        return thenApplyAsync( JavaFunction  { it  -> mapping(it) }, executor)
    }

    fun <T> CompletableFuture<T>.thenAcceptAsync(executor: Executor
                                                     , mapping: (T) -> Unit ) : CompletableFuture<Void> {
        return thenAcceptAsync( Consumer { mapping(it) }, executor)
    }

    val y2 = KCompletableFuture.supplyAsync(executor) { 10 }
            .thenApply { x ->
                val ret = x + 10
                log.info("Got $x returning $ret"); ret
            }
            .thenCompose { x ->
                CompletableFuture<Int>().also {
                    executor.schedule(2.seconds) {
                        val y = x * 10
                        log.info("Completing first future $x with $y")
                        it.complete(y)
                    }
                }
            }
            .thenApplyAsync(executor) { x ->
                val ret = x + 1
                log.info("Woot got $x returning $ret")
                ret
            }
            .thenComposeAsync(executor) { x ->
                CompletableFuture<String>().also {
                    executor.schedule(2.seconds) {
                        val ret = "Woot $x"
                        log.info("Completing second future $x with $ret")
                        it.complete(ret)
                    }
                }
            }
            .thenAcceptAsync(executor) {
                log.info("ret is $it")
            }
            .exceptionally { log.info("exception $it"); null }

    val z = CompletableFuture<Int>().apply { completeExceptionally(NumberFormatException()) }
    z.exceptionally { log.info("Async $z") ;0}
    listOf(y2).map { it.join() }
    executor.shutdown()
}
