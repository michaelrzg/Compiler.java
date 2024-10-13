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

## How to Run:

### To run a valid input example:

```sh
java driver.java src_valid.txt
```

### To an invalid input example:

```sh
java driver.java src_invalid.txt
```
