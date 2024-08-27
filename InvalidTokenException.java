// Name: Michael Rizig
// Class: CS 4308/W01
// Term: Fall 2024
// Instructor: Gayler
// Project Part 1
// File: Invalid Token Exception

// Exception thrown when an invalid token is given in the src. Returns row and column of invalid token
public class InvalidTokenException extends Exception {
    InvalidTokenException(int x, int y) {
        super("INVALID TOKEN AT LINE " + x + " COLUMN " + y);

    }
}
