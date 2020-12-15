fun <T, U : Any> List<T>.firstNotNull(transform: (T) -> U?): U = this.mapNotNull { transform(it) }.first()

fun List<Int>.product(): Long = this.fold(1L) { acc, i -> acc * i }
fun String.fromBinary(): Long = this.toLong(2)
fun Int.toBinary(): String = Integer.toBinaryString(this)
fun Long.toPowerOf(l: Long): Long = if (l == 0L) 1L else this * this.toPowerOf(l - 1L)
fun String.padToLength(length: Int): CharArray {
    val copy = this.toMutableList()
    while (copy.size < length) {
        copy.add(0, '0')
    }
    return copy.toCharArray()
}
inline fun <T> Iterable<T>.productBy(selector: (T) -> Long) = this.fold(1L) { acc, t -> acc * selector(t) }

val alphabet = 'a'..'z'