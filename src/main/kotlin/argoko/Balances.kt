package argoko

fun solution(balances: MutableList<Int>, requests: MutableList<String>): MutableList<Int> {

    for ( (idx, request) in requests.withIndex()) {
        val fail = mutableListOf( - idx - 1)
        val bits = request.split(" ")
        if ( bits.size !in listOf( 3,4) ) return fail

        val quantities = bits.drop(1).map { it.toInt()  }

        fun oob(x : Int) = x>0 && x< balances.size

        when(bits[0]) {
            "withdraw" -> {
                if (quantities.size != 2) return fail
                val idx = quantities[0] - 1
                if ( oob(idx) ) return fail
                balances[idx] -= quantities[1]
                if (balances[idx] < 0) return fail
            }
            "transfer" -> {
                val (from, to, amount) = quantities
                val (fromIdx, toIdx) = Pair( from - 1, to - 1)
                if ( oob(fromIdx) || oob(toIdx)) return fail
                balances[from-1] -= amount
                if (balances[from-1] < 0) return fail
                balances[to-1] += amount
            }
            "deposit" -> {
                val (account, amount) = quantities
                val accountIdx = account - 1
                if (oob(accountIdx) ) return fail
                balances[accountIdx] += amount
            }
            else -> return fail
        }
    }
    return balances
}
val x = """
You've been asked to program a bot for a popular bank that will automate the management of incoming requests. There are three types of requests the bank can receive:

transfer i j sum: request to transfer sum amount of money from the ith account to the jth one (i and j are 1-based);
deposit i sum: request to deposit sum amount of money in the ith account (1-based);
withdraw i sum: request to withdraw sum amount of money from the ith account (1-based).

Your bot should also be able to process invalid requests. There are two types of invalid requests:

invalid account number in the requests;
withdrawal / transfer of a larger amount of money than is currently available.

For the given list of balances and requests, return the state of balances after all requests have been processed, or an array of a single element [-<request_id>] (please note the minus sign), where <request_id> is the 1-based index of the first invalid request.

Example

For balances = [10, 100, 20, 50, 30] and
requests = ["withdraw 2 10", "transfer 5 1 20", "deposit 5 20", "transfer 3 4 15"],
the output should be solution(balances, requests) = [30, 90, 5, 65, 30].

Here are the states of balances after each request:
"withdraw 2 10": [10, 90, 20, 50, 30];
"transfer 5 1 20": [30, 90, 20, 50, 10];
"deposit 5 20": [30, 90, 20, 50, 30];
"transfer 3 4 15": [30, 90, 5, 65, 30], which is the answer.

For balances = [20, 1000, 500, 40, 90] and
requests = ["deposit 3 400", "transfer 1 2 30", "withdraw 4 50"],
the output should be solution(balances, requests) = [-2].

After the first request, balances becomes equal to [20, 1000, 900, 40, 90], but the second one turns it into [-10, 1030, 900, 40, 90], which is invalid. Thus, the second request is invalid, and the answer is [-2]. Note that the last request is also invalid, but it shouldn't be included in the answer.
"""