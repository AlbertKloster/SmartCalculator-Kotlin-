package calculator

import java.math.BigDecimal

val variables = mutableMapOf<String, String>()

fun main() {
    while (true) {
        val input = readln().trim()

        if (input == "/exit")
            break

        if (input == "/help") {
            println("The program calculates sum and subtraction of numbers")
            continue
        }

        if (input.matches(Regex("/.*"))) {
            println("Unknown command")
            continue
        }

        if (input.isEmpty())
            continue

        if (input.matches(Regex(".*\\w+\\s+\\w+.*"))) {
            println("Invalid expression")
            continue
        }

        try {
            if (input.contains('=')) {
                setVariable(input)
            } else {
                val (numbers, operations) = getCalculation(input)
                var result = BigDecimal.ZERO
                for (operationIndex in operations.indices) {
                    result = when (operations[operationIndex]) {
                        '-' -> result - numbers[operationIndex + 1].toBigDecimal()
                        '+' -> result + numbers[operationIndex + 1].toBigDecimal()
                        else -> BigDecimal.ZERO
                    }
                }
                println(result)
            }
        } catch (e: RuntimeException) {
            println(e.message)
            continue
        }
    }
    println("Bye!")
}

private fun setVariable(input: String) {
    val split = input.split(Regex("\\s*=\\s*"), 2)

    if (split.size != 2)
        throw RuntimeException("Invalid expression")

    val (identifier, assignment) = split

    if (!identifier.matches(Regex("[a-zA-Z]+")))
        throw RuntimeException("Invalid Identifier")

    if (!assignment.matches(Regex("[-+]?[a-zA-Z]+|[-+]?\\d+")))
        throw RuntimeException("Invalid assignment")

    if (assignment.matches(Regex("[-+]?\\d+")))
        variables[identifier] = assignment

    if (assignment.matches(Regex("[-+]?[a-zA-Z]+"))) {
        val assignmentVariable = assignment.replace(Regex("[-+]+"), "")
        val value = variables[assignmentVariable] ?: throw RuntimeException("Unknown variable")
        val assignmentValue = assignment.replace(assignmentVariable, value)
        variables[identifier] = assignmentValue
    }

}

private fun getCalculation(input: String): Calculation {

    val numbersAndVariables = mutableListOf<String>()
    val operations = mutableListOf<Char>()

    val sanitizedInput = "0+" + input.replace("\\s".toRegex(), "")

    var currentNumber = ""
    var lastOperation: Char? = null

    for (char in sanitizedInput) {
        if (char == '-' || char == '+') {
            if (currentNumber.isNotEmpty()) {
                numbersAndVariables.add(currentNumber)
                currentNumber = ""
            }
            lastOperation = if (lastOperation == null) {
                char
            } else if (char != lastOperation) {
                '-'
            } else {
                '+'
            }
        } else {
            currentNumber += char
            if (lastOperation != null) {
                operations.add(lastOperation)
                lastOperation = null
            }
        }
    }

    if (currentNumber.isNotEmpty())
        numbersAndVariables.add(currentNumber)

    if (lastOperation != null)
        operations.add(lastOperation)

    if (numbersAndVariables.size != operations.size + 1)
        throw RuntimeException("Invalid expression")

    val numbers = mutableListOf<String>()

    numbersAndVariables.forEach {
        if (it.matches(Regex("[a-zA-Z]+"))) {
            val value = variables[it]
            numbers.add(value ?: throw RuntimeException("Unknown variable"))
        } else if (it.matches(Regex("\\d+"))) {
            numbers.add(it)
        } else throw RuntimeException("Invalid identifier")
    }

    return Calculation(numbers, operations)

}