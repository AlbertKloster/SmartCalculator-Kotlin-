# Stage 5/8: Error!
## Description
Now you need to consider the reaction of the calculator when users enter expressions in the wrong format. The program only knows numbers, a plus sign, a minus sign, and two commands. It cannot accept all other characters and it is necessary to warn the user about this.

## Objectives
- The program should print `Invalid expression` in cases when the given expression has an invalid format. If a user enters an invalid command, the program must print `Unknown command`. All messages must be printed without quotes. The program must never throw an exception.
- To handle incorrect input, you should remember that the user input that starts with `/` is a <i>command</i>, in other situations, it is an <i>expression</i>.
- Like before, `/help` command should print information about your program. When the command `/exit` is entered, the program must print `Bye!` , and then stop.

## Examples
The greater-than symbol followed by a space (`> `) represents the user input.
```
> 8 + 7 - 4
11
> abc
Invalid expression
> 123+
Invalid expression
> +15
15
> 18 22
Invalid expression
>
> -22
-22
> 22-
Invalid expression
> /go
Unknown command
> /exit
Bye!
```
