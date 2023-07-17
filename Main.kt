package calculator

fun main() {
    while (true) {
        val input = readln().trim()
        if (input == "/exit") break
        if (input == "/help") {
            println("The program calculates sum and subtraction of numbers")
            continue
        }
        if (input.matches(Regex("/.*"))) {
            println("Unknown command")
            continue
        }
        if (input.isEmpty()) continue

        if (input.matches(Regex(".*\\d+\\s+\\d+.*"))) {
            println("Invalid expression")
            continue
        }

        try {
            val (numbers, operations) = getCalculation(input)
            var result = 0
            for (operationIndex in operations.indices) {
                result = when (operations[operationIndex]) {
                    '-' -> result - numbers[operationIndex + 1]
                    '+' -> result + numbers[operationIndex + 1]
                    else -> 0
                }
            }
            println(result)
        } catch (e: RuntimeException) {
            println(e.message)
            continue
        }
    }
    println("Bye!")
}


private fun getCalculation(input: String): Calculation {

    val numbers = mutableListOf<Int>()
    val operations = mutableListOf<Char>()

    val sanitizedInput = "0+" + input.replace("\\s".toRegex(), "")

    var currentNumber = ""
    var lastOperation: Char? = null

    for (char in sanitizedInput) {
        when {
            char.isDigit() -> {
                currentNumber += char
                if (lastOperation != null) {
                    operations.add(lastOperation)
                    lastOperation = null
                }
            }
            char == '-' || char == '+' -> {
                if (currentNumber.isNotEmpty()) {
                    numbers.add(currentNumber.toInt())
                    currentNumber = ""
                }
                lastOperation = if (lastOperation == null) {
                    char
                } else if (char != lastOperation) {
                    '-'
                } else {
                    '+'
                }
            }
            else -> throw IllegalArgumentException("Invalid expression")
        }
    }

    if (currentNumber.isNotEmpty()) {
        numbers.add(currentNumber.toInt())
    }

    if (lastOperation != null) {
        operations.add(lastOperation)
    }

    if (numbers.size != operations.size + 1)
        throw RuntimeException("Invalid expression")

    return Calculation(numbers.toTypedArray(), operations.toTypedArray())

}