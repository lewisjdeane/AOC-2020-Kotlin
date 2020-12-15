fun main() = Six().solve()

class Six : Day<Int, Int>(6) {

    private val groups: List<List<String>> by lazy { input.split("\n\n").map { it.lines() } }

    override fun a() = calculateCount(::predicateA)
    override fun b() = calculateCount(::predicateB)

    private fun calculateCount(predicate: (List<String>, Char) -> Boolean) =
            groups.sumBy { group -> alphabet.count { letter -> predicate(group, letter) } }

    private fun predicateA(group: List<String>, letter: Char) = group.any { line: String -> letter in line }
    private fun predicateB(group: List<String>, letter: Char) = group.all { line: String -> letter in line }
}
