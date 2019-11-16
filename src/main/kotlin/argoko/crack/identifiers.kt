package argoko.crack

import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

infix fun<T> List<T>.`±`(x : T ) = this + x

infix fun<T,R> T.`⫸`( fn : (T) -> R) = fn(this)

infix fun<T,R> T.`≫`( fn : T.() -> R) = fn(this)

fun<T> T.listify() = listOf(this)

infix fun<T,R> List<T>.map2( x : (T) -> R) = this.map(x)

val xs = listOf(4,5,6).map { it * it}

infix fun Int.ard( x : Int) = this + x
fun Int.plus( x : Int) = this + x
fun Int.div( x : Int) = this / x

inline fun <T, R> T.`⩥`(block: T.() -> R): R {
    return block()
}

fun main() {
    val xs = ( listOf(1,2,3) `±` 10) + (listOf(4,5,6) `±` 20)
    println(xs)
    val x = 10 `⫸` { it + 20 } `⫸` { it * 2 }
    val x2 = 10 `≫` { this ard 10 } `≫` { this / 3 } `≫` { listify() }
    val x3 = 10+10
            .div(3)
            .listify()
            .filter { it > 20 }
            .sum()
    println("x is $x")
    println("x3 is $x3")
}