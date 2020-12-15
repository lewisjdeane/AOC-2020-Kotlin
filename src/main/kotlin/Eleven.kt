fun main() = Eleven().solve()

class Eleven : Day<Int, Int>(11) {

    private val grid = inputLines.map { it.toList() }

    override fun a() = Part.A(grid).answer()
    override fun b() = Part.B(grid).answer()

    private sealed class Part(private val adjacencyThreshold: Int, private val grid: List<List<Char>>) {

        data class A(private val grid: List<List<Char>>) : Part(4, grid)

        data class B(private val grid: List<List<Char>>) : Part(5, grid)

        fun answer() = calculateFinalGrid().occupiedCount()

        private fun calculateFinalGrid(): List<List<Char>> {
            var round = 0
            var nextGrid = grid
            var currentGrid: List<List<Char>> = listOf()
            while (nextGrid != currentGrid) {
                currentGrid = nextGrid
                nextGrid = nextGrid(currentGrid)
                round++
            }
            return nextGrid
        }

        private fun nextGrid(prevGrid: List<List<Char>>): List<List<Char>> {
            val copy = prevGrid.toMutableList()

            for (i in prevGrid.indices) {
                for (j in prevGrid[0].indices) {
                    val nextSeat = nextSeat(prevGrid, i, j)
                    copy[i] = copy[i].take(j) + nextSeat + copy[i].drop(j + 1)
                }
            }

            return copy
        }

        private fun nextSeat(grid: List<List<Char>>, i: Int, j: Int): Char {
            if (grid[i][j].isFloor()) return floor

            val increments = listOf(
                    Pair(1, 1),
                    Pair(1, -1),
                    Pair(1, 0),
                    Pair(0, 1),
                    Pair(0, -1),
                    Pair(-1, 0),
                    Pair(-1, 1),
                    Pair(-1, -1))

            val adjacencyOccupiedCount = increments.count {
                try {
                    var x = i
                    var y = j
                    var isSeatInDirectionOccupied = false
                    do {
                        x += it.first
                        y += it.second
                        val seat = grid[x][y]
                        if (seat.isOccupied()) {
                            isSeatInDirectionOccupied = true
                            break
                        } else if (seat.isEmpty()) {
                            isSeatInDirectionOccupied = false
                            break
                        }
                    } while (this is B)
                    isSeatInDirectionOccupied
                } catch (e: IndexOutOfBoundsException) {
                    false
                }
            }

            return when {
                grid[i][j].isEmpty() && adjacencyOccupiedCount == 0 -> occupied
                grid[i][j].isOccupied() && adjacencyOccupiedCount >= adjacencyThreshold -> empty
                else -> grid[i][j]
            }
        }

        private fun List<List<Char>>.occupiedCount() = this.sumBy { row -> row.count { seat -> seat.isOccupied() } }
    }

    companion object {
        private const val floor = '.'
        private const val occupied = '#'
        private const val empty = 'L'

        fun Char.isFloor() = this == floor
        fun Char.isOccupied() = this == occupied
        fun Char.isEmpty() = this == empty
    }
}
