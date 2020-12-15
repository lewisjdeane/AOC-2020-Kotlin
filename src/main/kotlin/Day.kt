import java.io.File

abstract class Day<T, U>(private val dayNumber: Int) {

    abstract fun a(): T
    abstract fun b(): U

    fun solve() {
        println("Day number: $dayNumber")
        println("A: ${a()}")
        println("B: ${b()}")
    }

    val input = File("src/main/resources/$dayNumber.txt").readText()
    val inputLines: List<String> = input.lines()
}
