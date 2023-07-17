package calculator

fun main() {
    while (true) {
        val input = readln().trim()
        if (input == "/exit") break
        if (input == "/help") {
            println("The program calculates sum and subtraction of numbers")
            continue
        }
        if (input.isEmpty()) continue
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
    }
    println("Bye!")
}


private fun getCalculation(input: String): Calculation {
    val numbers = mutableListOf<Int>()
    val operations = mutableListOf<Char>()

    // Remove all whitespace from the input string and append prefix "0+"
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
            else -> throw IllegalArgumentException("Invalid character: $char")
        }
    }

    if (currentNumber.isNotEmpty()) {
        numbers.add(currentNumber.toInt())
    }

    return Calculation(numbers.toTypedArray(), operations.toTypedArray())

}