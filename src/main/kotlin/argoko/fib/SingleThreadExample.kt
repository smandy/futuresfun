package argoko.fib

import org.slf4j.LoggerFactory
import java.math.BigInteger

val log = LoggerFactory.getLogger("fje")!!

/** Single threads still win sometimes :-) **/
object Fib2 {
    private val ONE = 1.toBigInteger()
    private val cache = mutableMapOf(1 to ONE, 2 to ONE)
    operator fun invoke(n : Int) : BigInteger = cache.getOrPut(n) {
        this(n-1) + this(n-2)
    }
}

fun main() {
    log.info(Fib2(5000).toString())
}