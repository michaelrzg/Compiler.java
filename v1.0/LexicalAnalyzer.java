// Name: Michael Rizig
// Class: CS 4308/W01
// Term: Fall 2024
// Instructor: Gayler
// Project Part 1
// File: Lexical Analyzer

// class for lexical analyzer.
public class LexicalAnalyzer {

    // variables to store src, row and column
    private String src;
    private int pointerX;
    private int pointerY;

    // constructor (no default bc src string is required)
    public LexicalAnalyzer(String src) {
        this.src = src;
        pointerX = 1;
        pointerY = 1;
    }

    // main function of this object
    // returns next token in string as Token object
    public Token getToken() throws InvalidTokenException {

        // check if next input is valid or EOS
        if (pointerX >= src.length()) {
            return new Token('$', Types.EOS, pointerX++, pointerY++);
        }
        // move pointer to next non-white space
        while (src.charAt(pointerX) == ' ') {
            pointerX++;
        }
        // swich case on next non-white space char
        // If it matches one of our types, we return a new token, with:
        // char,type from types enum (see Types.java), row, and column
        switch (src.charAt(pointerX)) {
            case '+':
                return new Token(src.charAt(pointerX), Types.ADDITION, pointerX++, pointerY);
            case '-':
                return new Token(src.charAt(pointerX), Types.SUBTRACTION, pointerX++, pointerY);
            case '*':
                return new Token(src.charAt(pointerX), Types.MULTIPLICATION, pointerX++, pointerY);
            case '/':
                return new Token(src.charAt(pointerX), Types.DIVISION, pointerX++, pointerY);
            case '(':
                return new Token(src.charAt(pointerX), Types.LPAREN, pointerX++, pointerY);
            case ')':
                return new Token(src.charAt(pointerX), Types.RPAREN, pointerX++, pointerY);
            default:
                System.out.println("Invalid Token at Line" + pointerY + " Column " + pointerX);
            

        }
        // if its not one of the symbols above AND it is an ascii integer literal,
        // repeat above creation and pass it the int as a char
        if (src.charAt(pointerX) > 47 && src.charAt(pointerX) < 58) {
            return new Token(src.charAt(pointerX), Types.INTLIT, pointerX++, pointerY);
        } // if none of these cases, throw exception and track row and column.
        else
            throw new InvalidTokenException(pointerX, pointerY);

    }

}
