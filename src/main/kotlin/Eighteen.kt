fun main() = Eighteen().solve()

class Eighteen : Day<Long, Long>(18) {

    override fun a() = inputLines.map { Part.A.calculate(it) }.sum()
    override fun b() = inputLines.map { Part.B.calculate(it) }.sum()

    enum class Part(private val passes: List<Set<Operator>>) {
        A(listOf(setOf(Operator.PLUS, Operator.TIMES))),
        B(listOf(setOf(Operator.PLUS), setOf(Operator.TIMES)));

        fun calculate(s: String): Long {
            var tokens = s.replace("(", "( ").replace(")", " )").split(" ")
            while ("(" in tokens) {
                val indexOfNextCloseBracket = tokens.indexOfFirst { it == ")" }
                val indexOfMatchingOpenBracket = (indexOfNextCloseBracket downTo 0).find { tokens[it] == "(" }!!
                val subList = tokens.subList(indexOfMatchingOpenBracket + 1, indexOfNextCloseBracket)
                tokens = tokens.take(indexOfMatchingOpenBracket) + subList.doPasses(passes).toString() + tokens.drop(indexOfNextCloseBracket + 1)
            }
            return tokens.doPasses(passes)
        }

        private fun List<String>.doPasses(passes: List<Set<Operator>>): Long {
            var remainingElements = this
            passes.forEach { operatorsForPass ->
                remainingElements = remainingElements.doPass(operatorsForPass)
            }
            return remainingElements.first().toLong()
        }

        private fun List<String>.doPass(operators: Set<Operator>): List<String> {
            var tokens = this

            while (true) {
                val index = tokens.indexOfNextOperator(operators)

                if (index == -1) break // No more operators to process for this pass so we're done.

                val operator = Operator.fromSymbol(tokens[index])
                val x = tokens[index - 1].toLong() // LHS of operator.
                val y = tokens[index + 1].toLong() // RHS of operator.
                val next = operator.invoke(x, y)

                // Replace the three tokens used for the operation with the answer.
                tokens = tokens.take(index - 1) + next.toString() + tokens.drop(index + 2)
            }

            return tokens
        }

        private fun List<String>.indexOfNextOperator(operators: Set<Operator>) =
                this.indexOfFirst { token -> token in operators.map { it.symbol } }
    }

    enum class Operator(val symbol: String, private val perform: (Long, Long) -> Long) {
        PLUS("+", { x, y -> x + y }),
        TIMES("*", { x, y -> x * y });

        fun invoke(x: Long, y: Long) = perform.invoke(x, y)

        companion object {
            fun fromSymbol(symbol: String) = values().find { it.symbol == symbol }!!
        }
    }
}