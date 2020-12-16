fun main() = Sixteen().solve()

class Sixteen : Day<Int, Long>(16) {

    override fun a() = nearbyTickets.sumBy { it.invalidValues().sum() }

    override fun b(): Long {
        val departureIndices = calculateLocationNameToIndexMap().filterKeys { it.startsWith("departure") }.map { it.value }
        return departureIndices.productBy { myTicket.values[it].toLong() }
    }

    private val locations = inputLines.takeWhile { it.isNotEmpty() }.map { Location.fromRaw(it) }
    private val myTicket = Ticket.fromRaw(inputLines.drop(locations.size + 2).first())
    private val nearbyTickets = inputLines.dropWhile { it != "nearby tickets:" }.drop(1).map { Ticket.fromRaw(it) }
    private val validTickets = nearbyTickets.filter { it.invalidValues().isEmpty() }

    private fun calculateLocationNameToIndexMap(
        remainingLocations: MutableList<Location> = locations.toMutableList(),
        map: MutableMap<String, Int> = mutableMapOf()
    ): Map<String, Int> {
        return if (remainingLocations.isEmpty()) {
            map
        } else {
            var foundLocations: List<Location>
            var index = 0

            while (true) {
                foundLocations = findPossibleLocationsForIndex(remainingLocations, index)
                if (foundLocations.size == 1) {
                    break
                } else {
                    index++
                }
            }

            val matchedLocation = foundLocations.first()

            remainingLocations.remove(matchedLocation)
            map[matchedLocation.name] = index

            calculateLocationNameToIndexMap(remainingLocations, map)
        }
    }

    private fun findPossibleLocationsForIndex(locations: List<Location>, index: Int): List<Location> {
        val valuesAtIndex = validTickets.map { it.values[index] }
        return locations.filter { location -> valuesAtIndex.all { value -> location.isValidForValue(value) } }
    }

    private fun Ticket.invalidValues() =
            this.values.filter { value -> locations.none { location -> location.isValidForValue(value) } }

    data class Ticket(val values: List<Int>) {
        companion object {
            fun fromRaw(s: String) = Ticket(s.split(",").map { it.toInt() })
        }
    }

    data class Location(val name: String, val range1: String, val range2: String) {

        private val startRange1 = getStart(range1)
        private val endRange1 = getEnd(range1)
        private val startRange2 = getStart(range2)
        private val endRange2 = getEnd(range2)

        private fun getStart(s: String) = s.split("-").first().toInt()
        private fun getEnd(s: String) = s.split("-").last().toInt()

        fun isValidForValue(value: Int) = value in startRange1..endRange1 || value in startRange2..endRange2

        companion object {
            fun fromRaw(s: String): Location {
                val name = s.split(":").first()
                val ranges = s.split(":").last().trim()
                val rangeParts = ranges.split(" or ")
                return Location(name, rangeParts[0], rangeParts[1])
            }
        }
    }
}
