fun main() = Nine().solve()

class Nine : Day<Long, Long>(9) {

    private val numbers =  inputLines.map { it.toLong() }

    override fun a(): Long {
        return (0 until numbers.size - PREAMBLE_SIZE).first {
            val (preamble, following) = getParts(it)
            following !in pairSums(preamble)
        }.let { getParts(it).second }
    }

    override fun b(): Long {
        val target = a()
        val numbersBeforeTarget = numbers.takeWhile { it != target }
        val partitionSizes = (1 until numbersBeforeTarget.size).toList()
        val partition = partitionSizes.firstNotNull { size -> partitionForSizeOrNull(numbersBeforeTarget, size, target) }
        return partition.min()!! + partition.max()!!
    }

    private fun partitionForSizeOrNull(numbers: List<Long>, partitionSize: Int, target: Long) =
            numbers.partitionsOfSize(partitionSize).firstOrNull { it.sum() == target }

    private fun List<Long>.partitionsOfSize(size: Int) = (0..this.size - size).map { this.subList(it, it + size) }

    private fun pairSums(numbers: List<Long>): List<Long> {
        return numbers.map { number ->
            val copy = numbers.toMutableList()
            copy.remove(number)
            copy.map { it + number }
        }.flatten()
    }

    private fun getParts(index: Int): Pair<List<Long>, Long> {
        val preamble = numbers.subList(index, index + PREAMBLE_SIZE)
        val following = numbers[index + PREAMBLE_SIZE]
        return Pair(preamble, following)
    }

    companion object {
        private const val PREAMBLE_SIZE = 25
    }
}
