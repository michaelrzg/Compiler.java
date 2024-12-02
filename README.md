# V1

## Added Lexical Analyzer

- Tokenizes input
- Can detect and reject invalid tokens

## Added Custom Exceptions

- InvalidSourceCode thrown when Source code path is not valid
- InvalidTokenException thrown when an invalid token is detected

# V2

## Added Parser

- determines if input is syntactically valid.
- if invalid, can detect where the error occurs and points to it in context

## Changes:

### Driver

- added method to combine adjacent int literal tokens
  - This is so token (3) immediately followed by token (4) are considered as a token (34)
  - only combines int literals and not other types
- driver now adds all tokens to Queue and passes it to parser

# V3

## Evaluating Valid Expressions

- Added parse tree that takes root as argument and recursivly (LRV) parses through the tree and evaluates the expression.
- Returns numerical value of input equation

## Updated Parser

- After checking for validity, parser now generates nodes and passes them to parse tree.
- Added Functions for generating correct types of nodes from Tokens.

# V4

## General

- each line must end with a space (' ') in order for lines not to get mixed
- example:
  x = 7+6 (there is a space here)
  display x

## Variables

- Compiler can now recognize variables and store their values
- variables can be called back later and values fetched

## Printing

- reserved keyword display prints out a variables contents

## Input

- reserved keyword input takes in user unput (an integer) and stores it in a variable

## How to Run:

### To run a valid input example:

```sh
java driver.java src_valid.txt
```

### To an invalid input example:

```sh
java driver.java src_invalid.txt
```
