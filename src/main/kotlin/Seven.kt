fun main() = Seven().solve()

class Seven : Day<Int, Int>(7) {

    private val bags = inputLines.map { Bag.fromRaw(it) }
    private val targetBag = bags.find { it.colour == "shiny gold" }!!

    override fun a() = bags.filter { it != targetBag }.count { bag -> targetBag.isInBag(bag) }
    override fun b() = bags.find { it == targetBag }!!.bagCount() - 1

    private fun Bag.bagCount(): Int {
        return this.childBags.sumBy { child ->
            val childAsParentBag = bags.find { it.colour == child.colour }!!
            val bagCountOfChild = childAsParentBag.bagCount()
            child.number * bagCountOfChild
        } + 1
    }

    private fun Bag.isInBag(bag: Bag): Boolean {
        return when {
            this.colour == bag.colour -> true
            bag.childBags.isEmpty() -> false
            else -> {
                val childBagColours = bag.childBags.map { it.colour }
                val childBags = bags.filter { it.colour in childBagColours }
                childBags.any { childBag -> this.isInBag(childBag) }
            }
        }
    }

    class Bag(val colour: String, val number: Int, val childBags: List<Bag>) {
        companion object {
            fun fromRaw(s: String): Bag {
                val bagAndChildBags = s.split(" contain ")
                val bag = bagAndChildBags[0].replace(Regex(" bags?"), "").trim()
                val childBags = when (val rawChildBags = bagAndChildBags[1].dropLast(1)) {
                    "no other bags" -> emptyList()
                    else -> {
                        val bagsWithNumber = rawChildBags.split(", ").map { it.replace(Regex(" bags?"), "") }
                        bagsWithNumber.map { bagWithNumber ->
                            val number = bagWithNumber.takeWhile { it != ' ' }.trim().toInt()
                            val colour = bagWithNumber.dropWhile { it != ' ' }.trim()
                            Bag(colour, number, emptyList())
                        }
                    }
                }
                return Bag(bag, 1, childBags)
            }
        }
    }
}
