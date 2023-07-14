package calculator

fun main() {
    val (a, b) = readln().split(Regex("\\s+")).map { it.toInt() }
    print(a + b)
}
