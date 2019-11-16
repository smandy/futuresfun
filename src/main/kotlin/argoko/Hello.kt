package argoko

import org.apache.log4j.LogManager
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.function.Supplier


object Hello {
    val log = LogManager.getLogger( Hello::class.java)
}

val log = Hello.log

fun main() {
    val executors =  Executors.newCachedThreadPool()
    log.info("Main thread")
    CompletableFuture.supplyAsync {
        Thread.sleep(500)
        log.info("Woot")
        10
    }.handle {
        e, t -> log.info("Handling $e $t")
        e
    }.thenCompose {
        x -> CompletableFuture.supplyAsync( Supplier {
        log.info("Supplyng");
        x + 11 }, executors)
    }.thenCompose {
        x ->
        log.info("Composing")
        val ret = CompletableFuture<Int>()
        executors.submit { log.info("Returning from scheuler"); ret.complete(x+10) }
        ret
    }.thenCompose {
        x -> log.info("Composing")
        CompletableFuture.supplyAsync { Thread.sleep(1000);log.info("Supplying"); x * 2}
    }.thenCompose {
        x ->
        log.info("Returning completedfuture")
        CompletableFuture.completedFuture( x + 20)
    }.join()
}
