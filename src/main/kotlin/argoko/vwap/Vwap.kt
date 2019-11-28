package argoko.vwap

/**
 *
 * Playing with extension functions for arithmetic operations of vwap
 *
 */
data class QtyPx( val qty : Int, val px : Double) {
    companion object {
        val ZERO = QtyPx(0, 0.0)
    }
}

data class QtyVol(val qty : Int, val vol : Double) {
    companion object {
        val ZERO = QtyVol(0, 0.0)
    }

    fun vwap() = if (qty==0) QtyPx.ZERO else QtyPx(qty, vol / qty)

    operator fun plus( rhs : QtyPx) = QtyVol(this.qty + rhs.qty, this.vol + rhs.vol)
}

infix fun Int.at(px : Double) = QtyPx(this, px)

fun Iterable<QtyPx>.vwap() = fold(QtyVol.ZERO) { acc, it -> acc + it }.vwap()

fun main() {
    println( listOf(
            10 at 101.0,
            20 at 102.0).vwap() )
    println( setOf(
            10 at 101.0,
            20 at 103.2,
            30 at 104.0).vwap() )
}
