package co.touchlab.secondlib

fun addTwo(int: Int): Int {
    return int + 2
}
fun addTwoFloat(num: Float): Float {
    return num + 2.2f
}
fun addString(string: String): String {
    return  string + "string"
}

class CounterTest {
    private var index = 0

    fun increment(){
        index++
    }
}

fun incrementCounterArray(counters: Array<Int>) : Array<Int>{
    counters.forEachIndexed { index, i ->
        counters[index] = i + 1
    }
    return counters
}

fun incrementCounterList(counters: List<Int>) : List<Int> {
    counters.forEachIndexed { index, i ->
        print("TEST")
    }
    return counters
}

fun incrementCounterMutableList(counters: MutableList<Int>) : MutableList<Int> {
    counters.forEachIndexed { index, i ->
        counters[index] = i + 1
    }
    return counters
}

fun incrementCounterMutableMap(counters: MutableMap<String,Int>) : MutableMap<String,Int> {
    counters.forEach {
        counters[it.key] = it.value + 1
    }
    return counters
}