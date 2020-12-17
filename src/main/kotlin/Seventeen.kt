fun main() = Seventeen().solve()

class Seventeen : Day<Int, Int>(17) {

    override fun a() = solve(startVectors<Vector3D>())
    override fun b() = solve(startVectors<Vector4D>())

    private fun solve(initialMap: Map<Vector, Boolean>): Int {
        var map = initialMap
        repeat(CYCLE_COUNT) { map = map.next() }
        return map.count { it.value }
    }

    private fun Map<Vector, Boolean>.next(): Map<Vector, Boolean> {
        val newMap = mutableMapOf<Vector, Boolean>()
        this.keys.flatMap { it.neighbours() }.distinct().forEach { vector ->
            newMap[vector] = vector.nextOccupiedness(this)
        }
        return newMap
    }

    private inline fun <reified T : Vector> startVectors(): Map<Vector, Boolean> {
        val map: MutableMap<Vector, Boolean> = mutableMapOf()
        inputLines.forEachIndexed { yIndex, s ->
            s.toCharArray().forEachIndexed { xIndex, c ->
                val vector = when (T::class.java) {
                    Vector3D::class.java -> Vector3D(xIndex, yIndex, 0)
                    Vector4D::class.java -> Vector4D(xIndex, yIndex, 0, 0)
                    else -> error("Invalid Vector implementation")
                }
                map[vector] = c.isOccupied()
            }
        }
        return map.toMap()
    }

    private fun Char.isOccupied() = this == OCCUPIED

    interface Vector {
        fun neighbours(): List<Vector>

        fun nextOccupiedness(map: Map<Vector, Boolean>): Boolean {
            val isOccupied = map[this]
            val neighbourOccupiedCount = neighbours().count { neighbour -> map[neighbour] ?: false }

            return if (isOccupied == true) {
                neighbourOccupiedCount in listOf(2, 3)
            } else {
                neighbourOccupiedCount == 3
            }
        }
    }

    data class Vector3D(val x: Int, val y: Int, val z: Int) : Vector {
        override fun neighbours(): List<Vector> {
            val range = -1..1
            return range.flatMap { x ->
                range.flatMap { y ->
                    range.mapNotNull { z ->
                        if (x != 0 || y != 0 || z != 0) {
                            Vector3D(this.x + x, this.y + y, this.z + z)
                        } else null
                    }
                }
            }
        }
    }

    data class Vector4D(val x: Int, val y: Int, val z: Int, val w: Int) : Vector {
        override fun neighbours(): List<Vector> {
            val range = -1..1
            return range.flatMap { x ->
                range.flatMap { y ->
                    range.flatMap { z ->
                        range.mapNotNull { w ->
                            if (x != 0 || y != 0 || z != 0 || w != 0) {
                                Vector4D(this.x + x, this.y + y, this.z + z, this.w + w)
                            } else null
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val CYCLE_COUNT = 6
        private const val OCCUPIED = '#'
    }
}
