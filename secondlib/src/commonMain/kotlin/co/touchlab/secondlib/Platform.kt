package co.touchlab.secondlib

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

class CounterTest {
    private var index = 0

    fun increment(){
        index++
    }
}