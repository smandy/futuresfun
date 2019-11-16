package argoko.extensions

import java.util.concurrent.*
import java.util.function.Supplier

object KCompletableFuture {
    fun <T> supplyAsync(executor: Executor
                        , mapping: () -> T): CompletableFuture<T> {
        return CompletableFuture.supplyAsync(Supplier { mapping() }, executor)
    }
}

data class TU( val n : Long, val units: TimeUnit)

val Long.seconds: TU
    get() = TU(this, TimeUnit.SECONDS)

val Int.seconds: TU
    get() = toLong().seconds

fun ScheduledExecutorService.schedule(x : Long, tu : TimeUnit, f : () -> Unit) : ScheduledFuture<*> =
        schedule({f()}, x, tu)

fun ScheduledExecutorService.schedule(tu : TU, f : () -> Unit) : ScheduledFuture<*> =
        schedule({f()}, tu.n, tu.units)
