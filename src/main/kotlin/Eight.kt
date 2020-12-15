fun main() = Eight().solve()

class Eight : Day<Int, Int>(8) {

    private val instructions = inputLines.map { Instruction.fromRaw(it) }
    private val flipInstructionVariants = generateFlipVariants()

    override fun a() = EndCondition.DUPLICATE_INSTRUCTION.finalAcculumatorOrNull(instructions)!!
    override fun b() = flipInstructionVariants.firstNotNull { EndCondition.NO_MORE_INSTRUCTIONS.finalAcculumatorOrNull(it) }

    enum class EndCondition {
        DUPLICATE_INSTRUCTION,
        NO_MORE_INSTRUCTIONS;

        fun finalAcculumatorOrNull(instructions: List<Instruction>): Int? {
            val seenInstructionIndicies = mutableListOf(0)
            var state = State()
            while (true) {
                // Try and get the next instruction to execute.
                val instruction: Instruction
                try {
                    instruction = instructions[state.instructionIndex]
                } catch (e: IndexOutOfBoundsException) {
                    // Program has terminated.
                    return if (this == NO_MORE_INSTRUCTIONS) state.accumulator else null
                }

                // Execute the instruction.
                state = instruction.perform(state)

                // Check if we've already seen the instruction already.
                if (state.instructionIndex in seenInstructionIndicies) {
                    return if (this == DUPLICATE_INSTRUCTION) state.accumulator else null
                } else {
                    seenInstructionIndicies.add(state.instructionIndex)
                }
            }
        }
    }

    data class State(val instructionIndex: Int = 0, val accumulator: Int = 0)

    sealed class Instruction(open val value: Int) {

        abstract fun perform(state: State): State
        abstract fun flip(): Instruction

        data class NoOp(override val value: Int) : Instruction(value) {
            override fun perform(state: State) = state.copy(instructionIndex = state.instructionIndex+1)

            override fun flip(): Instruction = Jump(value)
        }

        data class Acc(override val value: Int) : Instruction(value) {
            override fun perform(state: State) =
                    state.copy(instructionIndex = state.instructionIndex+1, accumulator = state.accumulator + value)

            override fun flip(): Instruction = error("Can't flip Acc instruction")
        }

        data class Jump(override val value: Int) : Instruction(value) {
            override fun perform(state: State) = state.copy(instructionIndex = state.instructionIndex+value)

            override fun flip(): Instruction = NoOp(value)
        }

        companion object {
            fun fromRaw(s: String): Instruction {
                val parts = s.split(" ")
                val instructionCode = parts[0]
                val value = parts[1].toInt()
                return when (instructionCode) {
                    "nop" -> NoOp(value)
                    "jmp" -> Jump(value)
                    "acc" -> Acc(value)
                    else -> error("Invalid instruction: $instructionCode")
                }
            }
        }
    }

    private fun generateFlipVariants(): List<List<Instruction>> {
        // Work out indices of instructions that can be flipped.
        val indices = instructions.mapIndexedNotNull { index, instruction ->
            if (instruction !is Instruction.Acc) index else null
        }

        return indices.map { index ->
            instructions.toMutableList().also { copy -> copy[index] = copy[index].flip() }
        }
    }
}
