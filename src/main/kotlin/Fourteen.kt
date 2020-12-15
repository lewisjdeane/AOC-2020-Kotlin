fun main() = Fourteen().solve()

class Fourteen : Day<Long, Long>(14) {

    private val masksAndSetOperations = parseInput(inputLines)

    override fun a() = Part.A(masksAndSetOperations).solve()
    override fun b() = Part.B(masksAndSetOperations).solve()

    private fun parseInput(remainingLines: List<String>): List<MaskAndSetOperations> {
        return if (remainingLines.isEmpty()) {
            emptyList()
        } else {
            val linesToParseAsGroup = listOf(remainingLines.first()) + remainingLines.drop(1).takeWhile { !it.startsWith("mask") }
            val nextRemainingLines = remainingLines.drop(linesToParseAsGroup.size)
            listOf(MaskAndSetOperations.fromRaw(linesToParseAsGroup)) + parseInput(nextRemainingLines)
        }
    }

    private sealed class Part(
        open val masksAndSetOperations: List<MaskAndSetOperations>,
        private val charToNotUpdate: Char
    ) {
        data class A(override val masksAndSetOperations: List<MaskAndSetOperations>) : Part(masksAndSetOperations, 'X')
        data class B(override val masksAndSetOperations: List<MaskAndSetOperations>) : Part(masksAndSetOperations, '0')

        fun solve(): Long {
            val registers = mutableMapOf<Long, Long>()

            masksAndSetOperations.forEach { (mask, setOperations) ->
                setOperations.forEach { (index, value) ->
                    handleOperation(registers, mask, index, value)
                }
            }

            return registers.values.sum()
        }

        fun handleOperation(registers: MutableMap<Long, Long>, mask: Mask, index: Int, value: Int) {
            when (this) {
                is A -> registers[index.toLong()] = mask.applyTo(value).first()
                is B -> mask.applyTo(index).forEach { registers[it] = value.toLong() }
            }
        }

        fun Mask.applyTo(value: Int): List<Long> {
            val chars = value.toBinary().padToLength(this.length)
            this.indicesToBits.forEach { (index, char) -> chars.updateIndexToChar(index, char) }
            return chars.combinations().map { it.joinToString("").fromBinary() }
        }

        fun CharArray.updateIndexToChar(index: Int, char: Char) {
            val length = this.size
            if (char != charToNotUpdate) this[length - index - 1] = char
        }

        private fun CharArray.combinations(): List<CharArray> {
            val charCount = this.count { it == 'X' }
            val numberOfCombinations = 2L.toPowerOf(charCount.toLong())

            return (0 until numberOfCombinations).map {
                val replacementCombination = it.toInt().toBinary().padToLength(charCount)
                val copy = this.toMutableList()
                val indices = this.mapIndexed { index, c -> if (c == 'X') index else null }.filterNotNull()
                indices.forEachIndexed { indexIntoCombination, indexIntoOriginal ->
                    copy[indexIntoOriginal] = replacementCombination[indexIntoCombination]
                }
                copy.toCharArray()
            }
        }
    }

    private data class Mask(private val mask: String) {
        val length = mask.length
        val indicesToBits = mask.mapIndexed { index, c -> Pair(length - index - 1, c) }
    }

    private data class MaskAndSetOperations(val mask: Mask, val setOperations: List<SetOperation>) {
        companion object {
            fun fromRaw(lines: List<String>): MaskAndSetOperations {
                val mask = Mask(lines.first().split(" = ")[1])
                val setOperationsRaw = lines.drop(1)
                val setOperations = setOperationsRaw.map { line ->
                    val index = line.drop(4).takeWhile { it != ']' }.toInt()
                    val value = line.split(" = ")[1].toInt()
                    SetOperation(index, value)
                }
                return MaskAndSetOperations(mask, setOperations)
            }
        }
    }

    private data class SetOperation(val index: Int, val value: Int)
}
