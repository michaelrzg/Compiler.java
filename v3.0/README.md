# V3

## Evaluating Valid Expressions

- Added parse tree takes root as argument and recursivly (LRV) parses through the tree and evaluates the expression.
- Returns numerical value of input equation

## Updated Parser

- After checking for validity, parser now generates nodes and passes them to parse tree.
- Added Functions for generating correct types of nodes from Tokens.

## How to Run:

### To run a valid input example:

```sh
java driver.java src_valid.txt
```

### To an invalid input example:

```sh
java driver.java src_invalid.txt
```
