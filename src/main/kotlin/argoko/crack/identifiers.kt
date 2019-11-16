package argoko.crack

infix fun<T> List<T>.`±`(x : T ) = this + x

infix fun<T,R> T.`⫸`( fn : (T) -> R) = fn(this)

infix fun<T,R> T.`≫`( fn : T.() -> R) = fn(this)

fun<T> T.listify() = listOf(this)

infix fun<T,R> List<T>.map2( x : (T) -> R) = this.map(x)

val xs = listOf(4,5,6).map { it * it}

fun main() {
    val xs = ( listOf(1,2,3) `±` 10) + (listOf(4,5,6) `±` 20)
    println(xs)
    val x = 10 `⫸` { it + 20 } `⫸` { it * 2 }
    val x2 = ((10 `≫` { this + 10 }) `≫` { this / 3 } ) `≫` { listify() }

    println("x is $x")
}