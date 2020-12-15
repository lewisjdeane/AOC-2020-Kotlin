fun main() = Two().solve()

class Two : Day<Int, Int>(2) {

    override fun a() = passwordValidationRequests.count { it.isValidA }
    override fun b() = passwordValidationRequests.count { it.isValidB }

    private val passwordValidationRequests = inputLines.map { PasswordValidationRequest.fromRaw(it) }
}

private class PasswordValidationRequest(
    private val password: String,
    private val letter: Char,
    min: Int,
    max: Int
) {
    val isValidA = password.count { it == letter } in min..max

    val isValidB = isIndexValid(min-1).xor(isIndexValid(max-1))

    private fun isIndexValid(index: Int) = password[index] == letter

    companion object {
        fun fromRaw(s: String): PasswordValidationRequest {
            val parts = s.split(" ")
            val password = parts[2]
            val letter = parts[1].first()
            val numParts = parts[0].split("-").map { it.toInt() }
            val min = numParts[0]
            val max = numParts[1]
            return PasswordValidationRequest(password, letter, min, max)
        }
    }
}
