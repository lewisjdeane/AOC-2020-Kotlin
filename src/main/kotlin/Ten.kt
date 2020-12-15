fun main() = Ten().solve()

class Ten : Day<Int, Long>(10) {

    private val adapters = inputLines.map { it.toInt() }
    private val deviceJoltage = adapters.max()!! + 3
    private val allAdapters = adapters + listOf(deviceJoltage)
    private val adapterGaps = generateGaps()

    override fun a(): Int {
        val gapsOfOne = adapterGaps.count { it == 1 }
        val gapsOfThree = adapterGaps.count { it == 3 }
        return gapsOfOne * gapsOfThree
    }

    override fun b(): Long {
        val groups = calculateAdjacentSections(adapterGaps)
        return countPermutations(groups)
    }

    private fun generateGaps(): List<Int> {
        val joltages = mutableListOf(0)
        var joltage = 0
        while (joltage < deviceJoltage) {
            joltage = allAdapters.filter { it in joltage+1..joltage+3 }.min()!!
            joltages.add(joltage)
        }
        return joltages.sorted().zipWithNext { a, b -> b-a }
    }

    private fun calculateAdjacentSections(gaps: List<Int>): List<List<Int>> {
        val groups = mutableListOf<List<Int>>()

        var remainingGaps = gaps
        while (remainingGaps.isNotEmpty()) {
            remainingGaps = remainingGaps.dropWhile { it == 3 }
            val group = remainingGaps.takeWhile { it != 3 }
            groups.add(group)
            remainingGaps = remainingGaps.drop(group.size)
        }

        return groups.filter { it.isNotEmpty() }
    }

    private fun countPermutations(groups: List<List<Int>>) = groups.map { permutationCountForGroup(it) }.product()

    private fun permutationCountForGroup(group: List<Int>) =
            when (group.size) {
                1 -> 1
                2 -> 2
                3 -> 4
                4 -> 7
                else -> error("Group size too big")
            }
}
