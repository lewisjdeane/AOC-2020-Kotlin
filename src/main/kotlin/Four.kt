fun main() = Four().solve()

class Four : Day<Int, Int>(4) {
    private val passports = input.split("\n\n").map { Passport(it.replace("\n", " ")) }

    override fun a() = passports.count { it.isValidA() }
    override fun b() = passports.count { it.isValidB() }
}

private data class Passport(private val passportString: String) {

    fun isValidA() = REQUIRED_FIELDS.all { it in passportString }
    fun isValidB() = isValidA() && areAllValuesValid()

    private fun areAllValuesValid() = REQUIRED_FIELDS.all { isFieldValid(it) }

    private fun isFieldValid(field: String): Boolean {
        val value = getValueForField(field)
        return when (field) {
            "byr" -> value.toInt() in 1920..2002
            "iyr" -> value.toInt() in 2010..2020
            "eyr" -> value.toInt() in 2020..2030
            "hgt" ->
                if (value.takeLast(2) == "cm") {
                    value.dropLast(2).toInt() in 150..193
                } else {
                    value.dropLast(2).toInt() in 59..76
                }
            "hcl" -> value.first() == '#' && value.drop(1).isColor()
            "ecl" -> value in listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")
            "pid" -> value.length == 9
            else -> throw IllegalStateException("Invalid field: $field")
        }
    }

    private fun getValueForField(field: String) =
            passportString.split(field)[1].drop(1).takeWhile { it != ' ' }

    companion object {
        private val REQUIRED_FIELDS = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")
    }
}

private fun String.isColor() = this.length == 6 && this.all { it in '0'..'9' || it in 'a'..'z' }
