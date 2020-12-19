fun main() = Nineteen().solve()

class Nineteen : Day<Int, Int>(19) {

    private val rules = inputLines.takeWhile { it.isNotEmpty() }.map { parseRule(it) }.toMap()
    private val inputs = inputLines.takeLastWhile { it.isNotEmpty() }

    override fun a() = Part.A(rules).solve(inputs)
    override fun b() = Part.B(rules).solve(inputs)

    sealed class Part(private val rules: Map<Int, String>) {

        class A(rules: Map<Int, String>): Part(rules)
        class B(rules: Map<Int, String>): Part(rules)

        fun solve(inputs: List<String>): Int {
            val regex = applyRule(0).toRegex()
            return inputs.count { regex.matches(it) }
        }

        /** Generates a regex for the rule at index. */
        private fun applyRule(index: Int): String {
            val rule = rules[index] ?: error("Could not find rule $index")
            return when {
                // PART B special. The introduction of "8: 42 | 42 8" instead of just "8: 42" means that we have "(42)+"
                // instead of (42).
                this is B && index == 8 -> "${applyRule(42)}+"

                // PART B special. The introduction of "11: 42 31 | 42 11 31" instead of just "11: 42 31" means that we
                // have "ab" or "aabb" or "aaabbb" etc. This is hard to accomplish with regex unless I'm missing
                // something. For this we just manually build up a regex to cover 5 duplicates, testing shows that is
                // sufficient for this problem (the solution doesn't go any higher for more duplicates).
                this is B && index == 11 -> {
                    val a = applyRule(42)
                    val b = applyRule(31)
                    var regex = ""
                    repeat (5) { regex = "$a($regex)?$b" }
                    regex
                }

                // If we've hit a letter then no more recursion required, just return it.
                isLetter(rule) -> rule.drop(1).take(1)

                // Otherwise, we need to do some recursion. This also depends on whether or not there's multiple
                // branches we can go down. We handle all branches here, it may be one it may be two, but thankfully
                // joining with "|" character ensures the regex will handle this for us.
                else -> {
                    rule.split(" | ")

                        // Split each branch into list of integers.
                        .map { part -> part.split(" ").map { it.toInt() } }

                        // For each branch, apply the rule to each element of the branch and then join branches.
                        .joinToString("|") { part -> part.joinToString("") { applyRule(it) } }

                        // Wrap it in brackets so that regex handles this as a token it needs to see.
                        .let { "($it)" }
                }
            }
        }

        private fun isLetter(s: String) = s.contains("\"")
    }

    private fun parseRule(s: String): Pair<Int, String> {
        val ruleIndex = s.takeWhile { it != ':' }.toInt()
        val rule = s.dropWhile { it != ' ' }.drop(1)
        return ruleIndex to rule
    }
}