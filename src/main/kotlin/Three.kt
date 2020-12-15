fun main() = Three().solve()

class Three : Day<Long, Long>(3) {

    override fun a() = PART_A_SLOPE.treeCount(inputLines)
    override fun b() = PART_B_SLOPES.productBy { it.treeCount(inputLines) }

    companion object {
        private val PART_A_SLOPE = Slope(3, 1)
        private val PART_B_SLOPES = listOf(Slope(1, 1), Slope(3, 1), Slope(5, 1), Slope(7, 1), Slope(1, 2))
    }
}

data class Slope(private val stepX: Int, private val stepY: Int) {

    fun treeCount(forest: List<String>): Long {
        val positions = generateSequence(Pair(0, 0)) { it.next() }.takeWhile { it.second < forest.size }
        return positions.count {
            val x = it.first % forest[0].length // All rows are same length, so we just modulo any arbitrary row.
            val y = it.second
            forest[y][x] == '#'
        }.toLong()
    }

    private fun Pair<Int, Int>.next() = this.copy(this.first + stepX, this.second + stepY)
}
