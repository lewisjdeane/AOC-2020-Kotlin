import java.io.File

abstract class Day<T, U>(private val dayNumber: Int) {
    abstract fun a(): T
    abstract fun b(): U

    fun solve() {
        println("Day number: $dayNumber")
        println("A: ${a()}")
        println("B: ${b()}")
    }

    val inputLines: List<String> = File("src/main/resources/$dayNumber.txt").readLines()
}
