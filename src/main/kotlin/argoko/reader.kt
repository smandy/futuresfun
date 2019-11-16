package argoko

import java.io.FileReader

val xs = listOf("1", "2", "3", "4", "5")
val doit = listOf("foo",
        "bar",
        "baz")

fun main() {
    val x3 = mutableMapOf<String,MutableMap<String,MutableList<String>>>()
    fun <U,V,W>  MutableMap<U,MutableMap<V,MutableList<W>>>.get( s1 : U, s2 : V) : MutableList<W> =
            getOrPut(s1) { mutableMapOf() }
                    .getOrPut(s2) { mutableListOf() }
    x3.get("foo", "bar") += "Woot"
    println("x is $x3")
    data class Properties(val first : String,
                          val last : String,
                          val property : String)
    val properties = """
        Andy,Smith,Developer
        Andy,Sharp,Grenada
        John,Sharp,Developer
        John,Lennon,Singer
        John,Mccartyh,Irish
    """.trimIndent().split("\n")
            .map { it.split(",") }
            .map { (a,b,c) -> Properties(a,b,c) }
    properties.forEach { (a,b,c) -> x3.get(a,b) += c }
    x3.forEach{
        (first, rest) ->
        println("$first\n=========")
        rest.forEach { last, lst ->
            println("   $last -> ${lst.joinToString(",", "[", "]")}")
        }
    }

    val x = FileReader("/home/andy/rach/strip.csv")
            .readLines()
            .map { it.split(",") }
            .filter { it.size == 2}
            .mapNotNull { xs -> xs[0].toIntOrNull()?.let { Pair(it,xs[1]) } }
            .toMap()
    doit()
}

fun doit( s : String) : String {
    return s.toUpperCase()
}

data class Person( val first : String, val last : String, val age : Int) {
    override fun toString() = "| ${first.padEnd(10, ' ')} | ${last.padEnd(10, ' ')} | $age |"
}

fun <T>Collection<T>.dump(label : String) {
    println("\n\n$label\n=========================")
    forEach(::println)
}

fun doit() {
    val people = listOf(Person("Sophie", "Smith", 16),
            Person("Audrey", "Hind", 40))
    val (majors, minors) = people.partition { it.age > 18 }

    majors.dump("Majors")
    minors.dump("Minors")
}





