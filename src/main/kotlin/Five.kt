fun main() = Five().solve()

class Five : Day<Long, Long>(5) {

    private val seatIds = inputLines.map { it.toSeatId() }

    override fun a() = seatIds.max()!!
    override fun b(): Long {
        val prevSeatId = seatIds.sorted().zipWithNext().find { it.second - it.first == 2L }!!.first
        return prevSeatId + 1
    }

    private fun String.toSeatId() = this.toBinaryString().fromBinary()
    private fun String.toBinaryString() = this.replace(Regex("[BR]"), "1").replace(Regex("[FL]"), "0")
}
