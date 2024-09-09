// Name: Michael Rizig
// Class: CS 4308/W01
// Term: Fall 2024
// Instructor: Gayler
// Project Part 1
// File: Token

// class contains the Token object used by Lexical Analyzer
public class Token {
    // store basic data abt each token
    private Types type;
    private char lexeme;
    private int row;
    private int column;

    // constructorm takes in lexeme, its type (see Type.java), row, and column
    public Token(char value, Types type, int pointerX, int pointerY) {
        this.lexeme = value;
        this.type = type;
        this.row = pointerX;
        this.column = pointerY;
    }

    // default constructor
    public Token() {
    }

    // override of toString to standardize how printing token goes.
    @Override
    public String toString() {
        return "Token for " + String.valueOf(lexeme);
    }

    // getters for each class attribute.
    public char getLexeme() {
        return lexeme;
    }

    public Types getType() {
        return type;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }
}
