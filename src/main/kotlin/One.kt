fun main() = One().solve()

class One : Day<Int?, Int>(1) {

    private val costs = super.inputLines.map { it.toInt() }

    override fun a(): Int? = productForTarget()

    override fun b(): Int = costs.mapNotNull { cost -> productForTarget(TARGET - cost)?.let { it * cost } }.first()

    private fun productForTarget(target: Int = TARGET): Int? {
        return costs.find { cost -> target - cost in costs }?.let { it * (target - it) }
    }

    companion object {
        private const val TARGET = 2020
    }
}
