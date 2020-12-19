fun main() {
    val days = listOf(
            One(),
            Two(),
            Three(),
            Four(),
            Five(),
            Six(),
            Seven(),
            Eight(),
            Nine(),
            Ten(),
            Eleven(),
            Twelve(),
            Thirteen(),
            Fourteen(),
            Fifteen(),
            Sixteen(),
            Seventeen(),
            Eighteen(),
            Nineteen())

    days.forEach { it.solve() }
}