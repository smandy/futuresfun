package argoko

fun solution(crypt: MutableList<String>, solution: MutableList<MutableList<Char>>): Boolean {
    val mapping = solution.associate { it[0] to it[1] }


    fun leadingZeros( s : String) : Boolean {
        return s.length > 1 && String(s.map { mapping[it]!! }.toCharArray()).startsWith("0")
    }

    if (crypt.any { leadingZeros(it) } ) {
        return false
    }

    fun decode( s : String) : Int {
        return String(s.map { mapping[it]!! }.toCharArray()).toInt()
    }

    println(decode(crypt[0]))
    println(decode(crypt[1]))
    println(decode(crypt[2]))

    return decode(crypt[0]) + decode( crypt[1]) == decode( crypt[2])
}
