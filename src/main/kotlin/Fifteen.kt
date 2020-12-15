fun main() = Fifteen().solve()

class Fifteen : Day<Int, Int>(15) {

    private val startSequence = input.split(",").map { it.toInt() }

    override fun a() = findNthNumber(2020)
    override fun b() = findNthNumber(30_000_000)

    private fun findNthNumber(targetIndex: Int) = generateSpokenNumbers().drop(targetIndex - 1).first()

    private fun generateSpokenNumbers() = sequence {
        yieldAll(startSequence)
        val history = startSequence.mapIndexed { index, element -> element to index }.toMap().toMutableMap()
        var index = startSequence.size // First index is number of number of starting elements.
        var next = 0

        // Yield elements infinitely.
        while (true) {
            // Yield next.
            yield(next)

            // Work out when this last one was seen - it's not been saved to the history yet.
            val lastIndexSeenAt = history[next] ?: index

            // Save last index this number was seen at.
            history[next] = index

            // Calculate next gap.
            next = index - lastIndexSeenAt

            index++
        }
    }
}
