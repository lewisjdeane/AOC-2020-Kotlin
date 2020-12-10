fun <T, U : Any> List<T>.firstNotNull(transform: (T) -> U?): U? = this.mapNotNull { transform(it) }.first()

fun List<Int>.product(): Long = this.fold(1L) { acc, i -> acc * i }
