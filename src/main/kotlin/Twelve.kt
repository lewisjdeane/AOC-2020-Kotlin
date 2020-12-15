fun main() = Twelve().solve()

class Twelve : Day<Int, Int>(12) {

    private val movements = inputLines.map { Movement.fromRaw(it) }

    override fun a() = calculateFinalManhattanDistance(Part.A)
    override fun b() = calculateFinalManhattanDistance(Part.B)

    private fun calculateFinalManhattanDistance(part: Part): Int {
        var state = State()
        movements.forEach {
            state = if (part == Part.A) { it.moveA(state) } else { it.moveB(state) }
        }
        return state.position.manhattanDistance()
    }

    enum class Part {
        A, B;
    }

    data class State(
        val direction: Direction = Direction.East,
        val position: Pair<Int, Int> = Pair(0, 0),
        val waypoint: Pair<Int, Int> = Pair(10, 1)
    )

    enum class Direction {
        North,
        South,
        West,
        East;

        fun toPair(value: Int): Pair<Int, Int> {
            return when (this) {
                North -> Pair(0, value)
                South -> Pair(0, -value)
                West -> Pair(-value, 0)
                East -> Pair(value, 0)
            }
        }

        fun toPair2(waypoint: Pair<Int, Int>, value: Int): Pair<Int, Int> {
            return Pair(waypoint.first*value, waypoint.second*value)
        }

        fun rotateLeft(count: Int): Direction {
            return if (count == 0) {
                this
            } else {
                when (this) {
                    North -> West
                    West -> South
                    South -> East
                    East -> North
                }.rotateLeft(count-1)
            }
        }

        fun rotateRight(count: Int): Direction {
            return if (count == 0) {
                this
            } else {
                when (this) {
                    West -> North
                    North -> East
                    East -> South
                    South -> West
                }.rotateRight(count-1)
            }
        }
    }

    private sealed class Movement {

        abstract fun moveA(state: State): State

        abstract fun moveB(state: State): State

        fun rotateWaypointLeft(waypoint: Pair<Int, Int>, turns: Int): Pair<Int, Int> {
            return if (turns == 0) {
                waypoint
            } else {
                val newWaypoint = waypoint.copy(-waypoint.second, waypoint.first)
                rotateWaypointLeft(newWaypoint, turns - 1)
            }
        }

        fun rotateWaypointRight(waypoint: Pair<Int, Int>, turns: Int): Pair<Int, Int> {
            return if (turns == 0) {
                waypoint
            } else {
                val newWaypoint = waypoint.copy(waypoint.second, -waypoint.first)
                rotateWaypointRight(newWaypoint, turns - 1)
            }
        }

        data class North(val value: Int): Movement() {
            override fun moveA(state: State): State {
                return state.copy(position = state.position.copy(second = state.position.second + value))
            }

            override fun moveB(state: State): State {
                return state.copy(waypoint = state.waypoint.copy(second = state.waypoint.second + value))
            }
        }

        data class South(val value: Int): Movement() {
            override fun moveA(state: State): State {
                return state.copy(position = state.position.copy(second = state.position.second - value))
            }

            override fun moveB(state: State): State {
                return state.copy(waypoint = state.waypoint.copy(second = state.waypoint.second - value))
            }
        }

        data class East(val value: Int): Movement() {
            override fun moveA(state: State): State {
                return state.copy(position = state.position.copy(first = state.position.first + value))
            }

            override fun moveB(state: State): State {
                return state.copy(waypoint = state.waypoint.copy(first = state.waypoint.first + value))
            }
        }

        data class West(val value: Int): Movement() {
            override fun moveA(state: State): State {
                return state.copy(position = state.position.copy(first = state.position.first - value))
            }

            override fun moveB(state: State): State {
                return state.copy(waypoint = state.waypoint.copy(first = state.waypoint.first - value))
            }
        }

        data class Left(val value: Int): Movement() {
            override fun moveA(state: State): State {
                val turns = value / 90
                return state.copy(direction = state.direction.rotateLeft(turns))
            }

            override fun moveB(state: State): State {
                val newWaypoint = rotateWaypointLeft(state.waypoint, value/90)
                return state.copy(waypoint = newWaypoint)
            }
        }

        data class Right(val value: Int): Movement() {
            override fun moveA(state: State): State {
                val turns = value / 90
                return state.copy(direction = state.direction.rotateRight(turns))
            }

            override fun moveB(state: State): State {
                val newWaypoint = rotateWaypointRight(state.waypoint, value/90)
                return state.copy(waypoint = newWaypoint)
            }
        }

        data class Forward(val value: Int): Movement() {
            override fun moveA(state: State): State {
                val directionPair = state.direction.toPair(value)
                val newPair = Pair(state.position.first + directionPair.first, state.position.second + directionPair.second)
                return state.copy(position = newPair)
            }

            override fun moveB(state: State): State {
                val directionPair = state.direction.toPair2(state.waypoint, value)
                val newPair = Pair(state.position.first + directionPair.first, state.position.second + directionPair.second)
                return state.copy(position = newPair)
            }
        }

        companion object {
            fun fromRaw(s: String): Movement {
                val letter = s[0]
                val value = s.drop(1).toInt()
                return when (letter) {
                    'F' -> Forward(value)
                    'R' -> Right(value)
                    'L' -> Left(value)
                    'N' -> North(value)
                    'S' -> South(value)
                    'E' -> East(value)
                    'W' -> West(value)
                    else -> error("Invalid letter")
                }
            }
        }
    }
}
