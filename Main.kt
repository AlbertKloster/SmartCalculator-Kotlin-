package calculator

fun main() {
    while (true) {
        val input = readln().trim()
        if (input == "/exit") break
        if (input.isEmpty()) continue
        val numbers = input.split(Regex("\\s+")).map { it.toInt() }
        println(numbers.sum())
    }
    println("Bye!")
}
