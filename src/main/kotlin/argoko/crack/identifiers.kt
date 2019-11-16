package argoko.crack

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

fun<T> T.repeated(n : Int) = (0 until n).map { this }

var visibility = false
fun <T> T.debug(b : Boolean, block: (T) -> Unit) = if (b) { block(this); this } else this

fun main() {
    val xs = ( listOf(1,2,3) `±` 10) + (listOf(4,5,6) `±` 20)
    println(xs)
    val x = 10 `⫸` { it + 20 } `⫸` { it * 2 }
    val x2 = 10 `≫` { this ard 10 } `≫` { this / 3 } `≫` { listify() }
    val b = true
    val x3 = 10+10
            .debug(b) { println("add $it")}
            .div(3)
            .debug(b) { println("dev $it")}
            .repeated(10)
            .debug(b) { println("repeat $it")}
            .filter { it > 20 }
            .debug(b) { println("filter $it")}
            .sum()
            .debug(b) { println("sum $it")}
    println("x is $x")
    println("x3 is $x3")
}