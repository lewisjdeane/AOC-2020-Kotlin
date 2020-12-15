fun main() = Thirteen().solve()

class Thirteen : Day<Long, Long>(13) {

    private val allBuses = inputLines.last().split(",")
    private val buses = allBuses.filter { it != "x" }.map { it.toInt() }
    private val busesWithIndices = allBuses.mapIndexedNotNull { index, s -> if (s == "x") null else Pair(index.toLong(), s.toLong()) }
    private val currentMinute = inputLines.first().toLong()

    override fun a() =
        buses.map { busNumber ->
            busNumber to multiples(busNumber).first { multiple -> multiple > currentMinute } - currentMinute
        }.minBy { it.second }?.let { it.first * it.second }!!

    override fun b(): Long {
        val n = busesWithIndices.map { it.second }
        val a = busesWithIndices.map { it.second - it.first }
        return chineseRemainder(n, a)
    }

    private fun multiples(busNumber: Int) = generateSequence(busNumber) { it + busNumber }

    private fun chineseRemainder(n: List<Long>, a: List<Long>): Long {
        val product = n.reduce { acc, i -> acc * i }
        var sum = 0L
        n.indices.forEach { index ->
            val p = product / n[index]
            sum += a[index] * multiplicativeInverse(p, n[index]) * p
        }
        return sum % product
    }

    private fun multiplicativeInverse(_a: Long, _b: Long): Long {
        if (_b == 1L) {
            return 1
        }

        var a = _a
        var b = _b
        var x = 0L
        var y = 1L

        while (a > 1) {
            val quotient = a / b
            var t = b
            b = a % b
            a = t
            t = x
            x = y - quotient * x
            y = t
        }

        if (y < 0) {
            y += _b
        }

        return y
    }
}
