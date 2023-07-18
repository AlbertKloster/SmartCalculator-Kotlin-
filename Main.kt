package calculator

import java.math.BigInteger

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
                val infix = getInfix(input)
                val postfix = getPostfix(infix)
                val result = evaluatePostfix(postfix)

                println(result)
            }
        } catch (e: RuntimeException) {
            println(e.message)
            continue
        }
    }
    println("Bye!")
}

private fun evaluatePostfix(postfix: String): BigInteger {
    val stack = mutableListOf<BigInteger>()

    for (token in postfix.split(" ")) {
        when {
            token.isNumber() -> stack.add(token.toBigInteger())
            token.isOperator() -> {
                val operand2 = stack.removeLast()
                val operand1 = stack.removeLast()
                val result = performOperation(token[0], operand1, operand2)
                stack.add(result)
            }
        }
    }

    return stack.last()
}

private fun performOperation(operator: Char, operand1: BigInteger, operand2: BigInteger): BigInteger {
    return when (operator) {
        '+' -> operand1.add(operand2)
        '-' -> operand1.subtract(operand2)
        '*' -> operand1.multiply(operand2)
        '/' -> operand1.divide(operand2)
        '^' -> operand1.pow(operand2.toInt())
        else -> throw IllegalArgumentException("Invalid operator: $operator")
    }
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

private fun getPostfix(expression: String): String {
    val output = StringBuilder()
    val operatorStack = mutableListOf<Char>()

    var numberBuffer = StringBuilder()

    for (token in expression) {
        if (token.isDigit()) {
            numberBuffer.append(token)
        } else {
            if (numberBuffer.isNotEmpty()) {
                output.append(numberBuffer.toString())
                output.append(" ")
                numberBuffer = StringBuilder()
            }

            when {
                token.isOperator() -> {
                    while (operatorStack.isNotEmpty() &&
                        operatorStack.last().isOperator() &&
                        token.precedence() <= operatorStack.last().precedence()) {
                        output.append(operatorStack.removeLast())
                        output.append(" ")
                    }
                    operatorStack.add(token)
                }
                token == '(' -> operatorStack.add(token)
                token == ')' -> {
                    while (operatorStack.isNotEmpty() && operatorStack.last() != '(') {
                        output.append(operatorStack.removeLast())
                        output.append(" ")
                    }
                    if (operatorStack.isNotEmpty() && operatorStack.last() == '(') {
                        operatorStack.removeLast()
                    }
                }
                token == '^' -> {
                    while (operatorStack.isNotEmpty() &&
                        operatorStack.last() == '^' &&
                        token.precedence() <= operatorStack.last().precedence()) {
                        output.append(operatorStack.removeLast())
                        output.append(" ")
                    }
                    operatorStack.add(token)
                }
            }
        }
    }

    if (numberBuffer.isNotEmpty()) {
        output.append(numberBuffer.toString())
        output.append(" ")
    }

    while (operatorStack.isNotEmpty()) {
        output.append(operatorStack.removeLast())
        output.append(" ")
    }

    return output.toString().trim()
}

private fun Char.isOperator(): Boolean {
    return this == '+' || this == '-' || this == '*' || this == '/' || this == '^'
}

private fun Char.precedence(): Int {
    return when (this) {
        '+', '-' -> 1
        '*', '/' -> 2
        '^' -> 3
        else -> 0
    }
}

private fun String.isNumber(): Boolean {
    return try {
        this.toBigDecimal()
        true
    } catch (e: NumberFormatException) {
        false
    }
}

private fun String.isOperator(): Boolean {
    return this.length == 1 && this[0] in "+-*/^"
}

private fun getInfix(input: String): String {
    val infix = StringBuilder()
    val sanitizedInput = replaceAllVariables("0+" + input.replace("\\s".toRegex(), ""))

    var currentNumber = ""
    var lastOperation: Char? = null

    for (char in sanitizedInput) {
        if (char == '-' || char == '+') {
            if (currentNumber.isNotEmpty()) {
                appendCurrentNumber(infix, currentNumber)
                currentNumber = ""
            }
            lastOperation = if (lastOperation == null) {
                char
            } else if (char != lastOperation) {
                '-'
            } else {
                '+'
            }
        } else if (char == '*' || char == '/' || char == '^') {
            if (currentNumber.isNotEmpty()) {
                appendCurrentNumber(infix, currentNumber)
                currentNumber = ""
            }
            lastOperation = if (lastOperation == null) {
                char
            } else  throw RuntimeException("Invalid expression")
        } else if (char == '(' || char == ')') {
            if (currentNumber.isNotEmpty()) {
                appendCurrentNumber(infix, currentNumber)
                currentNumber = ""
            }
            if (lastOperation != null) {
                infix.append(lastOperation)
                lastOperation = null
            }
            infix.append(char)
        } else {
            currentNumber += char
            if (lastOperation != null) {
                infix.append(lastOperation)
                lastOperation = null
            }
        }
    }

    if (currentNumber.isNotEmpty())
        appendCurrentNumber(infix, currentNumber)

    if (lastOperation != null)
        infix.append(lastOperation)

    if (!infix.last().isDigit() && infix.last() != ')')
        throw RuntimeException("Invalid expression")

    if (infix.count { it == '(' } != infix.count { it == ')' })
        throw RuntimeException("Invalid expression")

    return infix.toString()
}

private fun appendCurrentNumber(infix: java.lang.StringBuilder, currentNumber: String) {
    if (currentNumber.matches(Regex("[a-zA-Z]+"))) {
        val value = variables[currentNumber]
        infix.append(value ?: throw RuntimeException("Unknown variable"))
    } else if (currentNumber.matches(Regex("\\d+"))) {
        infix.append(currentNumber)
    } else throw RuntimeException("Invalid identifier")
}

private fun replaceAllVariables(infix: String): String {
    var replacedInfix = infix
    while (true) {
        if (replacedInfix.contains("[A-Za-z]".toRegex())) {
            val variable = Regex("[A-Za-z]+").find(replacedInfix)!!.value
            if (variables.containsKey(variable)) {
                replacedInfix = replacedInfix.replace(variable, variables[variable]!!)
            } else {
                throw RuntimeException("Unknown variable")
            }
        } else return replacedInfix
    }
}