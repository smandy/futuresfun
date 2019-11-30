package argoko.fib

import com.google.gson.GsonBuilder
import org.slf4j.LoggerFactory
import java.io.FileWriter
import java.math.BigInteger

val log = LoggerFactory.getLogger("fje")!!


interface Calc {
    fun calc() : BigInteger
}

class Fib1(val n : Int) : Calc {
    private val ONE = 1.toBigInteger()
    //private val cache = HashMap<Int,BigInteger>(n).apply { set(1,ONE); set(2,ONE) }
    private val cache = hashMapOf( 1 to ONE, 2 to ONE)

    override fun calc() : BigInteger {
        (1..n).forEach { _calc(it) }
        return _calc(n)
    }

    fun _calc(x : Int = n) : BigInteger = cache.getOrPut(x) {
        //log.info("Calc $x")
        _calc(x-1) + _calc(x-2)
    }
}


/**
 *
 * Single threads still win sometimes :-)
 *
 * Forkjoin was a disaster for large N :-(
 *
 **/
class Fib2(val n : Int) : Calc {
    private val ONE = 1.toBigInteger()
    private val cache = HashMap<Int,BigInteger>(n).apply { set(1,ONE); set(2,ONE) }

    //private val cache = hashMapOf( 1 to ONE, 2 to ONE)

    override fun calc() : BigInteger {
        (1..n).forEach { _calc(it) }
        return _calc(n)
    }

    fun _calc(x : Int = n) : BigInteger = cache.getOrPut(x) {
        //log.info("Calc $x")
        _calc(x-1) + _calc(x-2)
    }
}

class Fib3(val n : Int) : Calc {
    private val ONE = 1.toBigInteger()
    val MINUS_ONE = -1.toBigInteger()
    private val cache = Array(n+1) { MINUS_ONE }.also { it[1] = ONE; it[2] = ONE }

    override fun calc() : BigInteger {
        (1..n).forEach { _calc(it) }
        return _calc(n)
    }

    fun _calc(x : Int = n) : BigInteger = cache[x].let {
        if (it == MINUS_ONE) {
            val ret = _calc(x-1) + _calc(x-2)
            cache[x] = ret
            ret
        } else {
            it
        }
    }
}

class Fib4(val n : Int) : Calc {
    private val ONE = 1.toBigInteger()
    private val cache = Array<BigInteger?>(n+1) { null }
            .also {
                it[1] = ONE;
                it[2] = ONE
            }

    override fun calc() : BigInteger {
        (1..n).forEach { _calc(it) }
        return _calc(n)
    }

    fun _calc(x : Int = n) : BigInteger = cache[x].let {
        if (it == null ) {
            val ret = _calc(x-1) + _calc(x-2)
            cache[x] = ret
            ret
        } else {
            it
        }
    }
}

class Fib5(val n : Int) : Calc {
    private val ONE = 1.toBigInteger()
    private val cache = Array<BigInteger?>(n+1) { null }
            .also {
                it[1] = ONE;
                it[2] = ONE
            }

    override fun calc() : BigInteger = if ( n==1 || n==2 ) {
        ONE
    } else {
        (1..n).forEach { _calc(it) }
        _calc(n)
    }

    fun _calc(x : Int = n) : BigInteger = cache[x].let {
        if (it == null ) {
            val ret = _calc(x-1) + _calc(x-2)
            cache[x] = ret
            ret
        } else {
            it
        }
    }
}



fun<U> timeIt( f : () -> U): Pair<U, Long> {
    val start = System.nanoTime()
    val ret = f()
    val duration = System.nanoTime() - start
    return Pair(ret, duration)
}

data class Result( val results : Map<Long, Int>,
                   val name : String,
                   val fmt : String)

fun calcResults( x : Calc,
                 iters : Int, name : String): Map<Long, Int> {
    /**
     * We use an array here to get reference semantics for the map's value
     */
    val stats = mutableMapOf<Long,IntArray>()
    for (iter in 0 until iters) {
        val result = timeIt { x.calc() }
        if (iter > 10000) {
            stats.getOrPut(result.second) { intArrayOf(0) }[0]++
        }
        if (iter % 10000 == 0) log.info("$name $iter -> ${result.second}....")
    }
    return stats.mapValues { it.value.first()}
}

data class Source( val calc : Calc , val fmt : String, val name : String)

fun main() {
    val (fibn, iters) = if (true) {
        Pair(50000, 100000)
    } else {
        Pair(100, 100)
    }

    val gson = GsonBuilder().setPrettyPrinting().create()!!
    listOf( Source(Fib1(fibn), "bx-", "fib1"),
            Source(Fib2(fibn), "rx-", "fib2"),
            Source(Fib3(fibn), "gx-","fib3"),
            Source(Fib4(fibn), "mx-", "fib4"),
            Source(Fib5(fibn), "kx-", "fib5")
    ).map {
        Result(calcResults(it.calc, iters, it.name), it.name, it.fmt)
    }.also {
        FileWriter("/tmp/stats.json").use {fw ->
            gson.toJson(it, fw)
        }
    }
}