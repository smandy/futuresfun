package argoko.extensions

import java.util.concurrent.*
import java.util.function.Supplier

object KCompletableFuture {
    /**
     * Kotlin friendly version of supplyAsync.
     *
     * This function is syntactic sugar to allow specifying the lambda with kotlin block syntax.
     */
    fun <T> supplyAsync(executor: Executor
                        , mapping: () -> T): CompletableFuture<T> {
        return CompletableFuture.supplyAsync(Supplier { mapping() }, executor)
    }
}

/**
 * An abstraction over units + unittype to allow dsl-style stuff like 2.seconds
 *
 * Just a pity they didn't migrate the executors API to use duration as there's a name clash here.
 */
data class TU( val n : Long, val units: TimeUnit)

val Long.seconds: TU
    get() = TU(this, TimeUnit.SECONDS)

val Int.seconds: TU
    get() = toLong().seconds

fun ScheduledExecutorService.schedule(x : Long, tu : TimeUnit, f : () -> Unit) : ScheduledFuture<*> =
        schedule({f()}, x, tu)

fun ScheduledExecutorService.schedule(tu : TU, f : () -> Unit) : ScheduledFuture<*> =
        schedule({f()}, tu.n, tu.units)
