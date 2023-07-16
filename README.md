# Stage 3/8: Count them all
## Description
In rare cases, we need to calculate the sum of only two numbers. Now it is time to teach the calculator to read an unlimited sequence of numbers. Also, let's take care of ourselves if after a while we want to remember what our program does. For this purpose, we'll introduce a new command `/help` to our calculator. Users who have first exposure to this program may use `/help` as well to know more about it!

## Objectives
- Add to the calculator the ability to read an unlimited sequence of numbers.
- Add a `/help` command to print some information about the program.
- If you encounter an empty line, do not output anything.

## Examples
The greater-than symbol followed by a space (`> `) represents the user input.
```
> 4 5 -2 3
10
> 4 7
11
> 6
6
> /help
The program calculates the sum of numbers
> /exit
Bye!
```
