import java.io.FileReader

fun main() {
    FileReader("/home/andy/twonk")
            .readLines()
            .flatMap { it.split(" ") }
            .map { it.trim() }
            .filter { !it.isEmpty() }
            .groupBy { it }
            .mapValues { it.value.size }
            .forEach { (q, v) -> println("%2d : %s".format(v,q) )}
}