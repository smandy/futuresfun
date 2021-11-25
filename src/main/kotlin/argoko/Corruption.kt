package argoko

fun solution(sourceArray: MutableList<Int>, destinationArray: MutableList<Int>): MutableList<Int> {
    var currentStart : Int? = null
    var best : MutableList<Int>? = null

    val iter = sourceArray.zip(destinationArray).withIndex()
    for ( (idx, srcDest) in iter ) {
        val (src, dest) = srcDest
        val good = src==dest
        //println( "$idx $srcDest cs=$currentStart best=$best good=$good" )

        val sentinel = idx==sourceArray.size - 1
        when {
            currentStart == null && good -> {
                if (sentinel) {
                    if (best == null) {
                        best = mutableListOf( 1, idx)
                    }
                }
            }
            currentStart != null && (!good || sentinel) -> {
                // Check to see if best
                val length = if (sentinel) ((idx - currentStart) + 1) else idx - currentStart
                val bestList = mutableListOf(length, currentStart)
                if (best==null) {
                    best = bestList
                } else {
                    if ( length>best[0]) {
                        best = bestList
                    }
                }
                currentStart = null
            }
        }
    }
    return best ?: mutableListOf(0,0)
}
val t2 = """
Input:

sourceArray: [92988800, 80253955, 17396563, 91682092, 77708269, 97587946, 23889892, 20661856, 21013095, 92028000, 17562863, 86804822, 17819093, 97941923, 64955308]
destinationArray: [92988800, 80253955, 17396563, 91682092, 77708229, 97587946, 23889892, 20661866, 21013095, 92928000, 17962863, 86804822, 14819093, 97241923, 62955308]

Output:

[5, 0]

Expected Output:

[4, 0]
"""


val x2 = """
    nput:

    sourceArray: [33531593, 96933415, 28506400, 39457872, 29684716, 86010806]
    destinationArray: [33531593, 96913415, 28506400, 39457872, 29684716, 86010806]

    Output:

    [1, 0]

    Expected Output:

    [4, 2]
""".trimIndent()

fun main() {
    val sourceArray = mutableListOf(92988800, 80253955, 17396563, 91682092, 77708269, 97587946, 23889892, 20661856, 21013095, 92028000, 17562863, 86804822, 17819093, 97941923, 64955308)
    val destinationArray = mutableListOf(92988800, 80253955, 17396563, 91682092, 77708229, 97587946, 23889892, 20661866, 21013095, 92928000, 17962863, 86804822, 14819093, 97241923, 62955308)

    println( solution(sourceArray, destinationArray) )
}

fun main2() {
    val sourceArray = mutableListOf(33531593, 96933415, 28506400, 39457872, 29684716, 86010806)
    val destinationArray = mutableListOf(33531593, 96913415, 28506400, 39457872, 29684716, 86010806)
    println( solution(sourceArray, destinationArray) )
}