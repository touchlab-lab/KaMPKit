package co.touchlab.kampstarter

import co.touchlab.secondlib.CounterTest

expect fun currentTimeMillis(): Long

internal expect fun printThrowable(t: Throwable)

expect fun currentPlatform(): String

fun addTwo(int: Int): Int {
    return int + 2
}
fun addTwoFloat(num: Float): Float {
    return num + 2.2f
}
fun addString(string: String): String {
    return  string + "string"
}

fun createAndIncrement() : CounterTest {
    val counter = CounterTest()
    counter.increment()
    return counter
}

fun incrementCounter(counter: CounterTest) : CounterTest {
    counter.increment()
    return counter
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