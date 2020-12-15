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
            Fourteen())

    days.forEach { it.solve() }
}