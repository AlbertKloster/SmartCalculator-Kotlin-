# Stage 4/8: Add subtractions
## Description
Finally, we got to the next operation: subtraction. It means that from now on the program must receive the addition `+` and subtraction `-` operators as an input to distinguish operations from each other. It must support both unary and binary minus operators. Moreover, If the user has entered several same operators following each other, the program still should work (like Java or Python REPL). Also, as you remember from school math, two adjacent minus signs turn into a plus. Therefore, if the user inputs `--`, it should be read as `+`; if they input `----`, it should be read as `++`, and so on. The smart calculator ought to have such a feature.

Pay attention to the `/help` command, it is important to maintain its relevance depending on the changes (in the next stages too). You can write information about your program in free form, but the main thing is that it should be understandable to you and other users.

## Objectives
- The program must calculate expressions like these: `4 + 6 - 8`, `2 - 3 - 4`, and so on.
- Modify the result of the `/help` command to explain these operations.
- Decompose your program using functions to make it easy to understand and edit later.
- The program should not stop until the user enters the `/exit` command.
- If you encounter an empty line, do not output anything.

## Examples
The greater-than symbol followed by a space (`> `) represents the user input.

```
> 8
8
> -2 + 4 - 5 + 6
3
> 9 +++ 10 -- 8
27
> 3 --- 5
-2
> 14       -   12
2
> /exit
Bye!
```
