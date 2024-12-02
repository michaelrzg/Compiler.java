// Name: Michael Rizig
// Class: CS 4308/W01
// Term: Fall 2024
// Instructor: Gayler
// Project Part 4
// File: Parser
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
final class Parser {

    // Queue to hold tokens
    private final Queue<Token> tokens;
    // Stack to keep track of left parenthesis closure
    private final Stack<Token> parenthesis;
    // Full source string for error pointing
    private final String src;
    // Memory for storing variable values
    private final Memory memory;

    // Scanner for reading input from the user
    private final Scanner scanner;

    /**
     * Constructor for Parser.
     *
     * @param allTokens Queue of all tokens in order
     * @param src       String containing all code in file
     */
    public Parser(Queue<Token> allTokens, String src) throws InvalidTokenException, Exception {
        tokens = allTokens;
        parenthesis = new Stack<>();
        this.src = src;
        this.memory = new Memory(128);
        this.scanner = new Scanner(System.in); // Initialize the scanner for user input

        // Start parsing the input
        while (!tokens.isEmpty() && tokens.peek().getType() != Types.EOS) {
            statement();
        }

        System.out.println("Parser ran with no errors found.");
    }

    /**
     * Parses a statement (either an assignment, expression, display, or input statement).
     */
    private void statement() throws Exception {
        Token current = tokens.peek();

        // Check if the current token is a display statement
        if (tokens.size() > 1 && tokens.peek().getLexeme().equals("display")) {
            display();
        } else if (tokens.size() > 1 && tokens.peek().getLexeme().equals("input")) {
            input();
        } else if (current.getType() == Types.ID) {
            // If it's an identifier, check for assignment or declaration
            assignment();
        } else {
            // Handle an expression and print its result
            Node expr = expression();
            int result = evaluate(expr);
            System.out.println("Expression value: " + result);
        }
    }

    /**
     * Parses a display statement (display <id>).
     */
    private void display() throws Exception {
        match(tokens.peek(), Types.DISPLAY); // Match the "display" keyword
        
        // Ensure the next token is an identifier
        if (tokens.isEmpty() || tokens.peek().getType() != Types.ID) {
            throw new Exception("Expected an identifier after 'display', found " + (tokens.isEmpty() ? "EOF" : tokens.peek().getType()));
        }

        Token idToken = tokens.poll(); // Fetch the identifier (variable name)
        String variableName = idToken.getLexeme();
        String value = memory.fetch(variableName); // Fetch value from memory
        
        if (value == null) {
            throw new Exception("Variable '" + variableName + "' is not initialized.");
        }
        
        // Display the value of the variable stored in memory
        System.out.println("Value stored at memory location " + variableName + " is " + value);
    }

    /**
     * Parses an input statement (input <id>).
     */
    private void input() throws Exception {
        match(tokens.peek(), Types.INPUT); // Match the "input" keyword

        // Ensure the next token is an identifier
        if (tokens.isEmpty() || tokens.peek().getType() != Types.ID) {
            throw new Exception("Expected an identifier after 'input', found " + (tokens.isEmpty() ? "EOF" : tokens.peek().getType()));
        }

        Token idToken = tokens.poll(); // Fetch the identifier (variable name)
        String variableName = idToken.getLexeme();

        // Prompt user for input
        System.out.print("Enter an integer value for " + variableName + ": ");
        String userInput = scanner.nextLine();

        try {
            // Try to parse the input as an integer
            int value = Integer.parseInt(userInput);
            memory.save(variableName, String.valueOf(value)); // Save value to memory
            System.out.println("Value entered is stored in memory location " + variableName);
        } catch (NumberFormatException e) {
            // Terminate if input is not a valid integer
            System.out.println("Invalid input. Terminating program.");
            System.exit(1);
        }
    }

    /**
     * Parses an assignment or declaration statement.
     */
    private void assignment() throws Exception {
        Token idToken = tokens.poll();
        if (idToken.getType() != Types.ID) {
            throw new Exception("Expected an identifier, found " + idToken.getType());
        }

        // Check if it's an assignment (x = ...)
        if (!tokens.isEmpty() && tokens.peek().getType() == Types.ASSIGNMENT) {
            match(tokens.peek(), Types.ASSIGNMENT);
            Node expr = expression();
            int value = evaluate(expr);
            memory.save(idToken.getLexeme(), String.valueOf(value));
            System.out.println("Assigned " + value + " to variable " + idToken.getLexeme());
        } else {
            // If no `=`, declare the variable with a default value (e.g., "null")
            memory.save(idToken.getLexeme(), null);
            System.out.println("Declared variable " + idToken.getLexeme() + " with no initial value.");
        }
    }

    /**
     * Parses an expression
     */
    public Node expression() throws Exception {
        Node termNode = term();
        return expression_prime(termNode);
    }

    private Node expression_prime(Node expr) throws Exception {
        if (tokens.isEmpty() || tokens.element().getType() == null) {
            return expr;
        } else switch (tokens.element().getType()) {
            case ADDITION -> {
                match(tokens.element(), Types.ADDITION);
                Node termNode = term();
                Node expr1 = new BinaryExpressionNode(expr, Types.ADDITION, termNode);
                return expression_prime(expr1);
            }
            case SUBTRACTION -> {
                match(tokens.element(), Types.SUBTRACTION);
                Node termNode = term();
                Node expr1 = new BinaryExpressionNode(expr, Types.SUBTRACTION, termNode);
                return expression_prime(expr1);
            }
            default -> {
                return expr;
            }
        }
    }

    private Node term() throws Exception {
        Node factorNode = factor();
        return term_prime(factorNode);
    }

    private Node term_prime(Node term) throws Exception {
        if (tokens.isEmpty() || tokens.element().getType() == null) {
            return term;
        } else switch (tokens.element().getType()) {
            case MULTIPLICATION -> {
                match(tokens.element(), Types.MULTIPLICATION);
                Node factorNode = factor();
                Node term1 = new BinaryExpressionNode(term, Types.MULTIPLICATION, factorNode);
                return term_prime(term1);
            }
            case DIVISION -> {
                match(tokens.element(), Types.DIVISION);
                Node factorNode = factor();
                Node term1 = new BinaryExpressionNode(term, Types.DIVISION, factorNode);
                return term_prime(term1);
            }
            default -> {
                return term;
            }
        }
    }

    private Node factor() throws Exception {
        if (tokens.isEmpty() || tokens.element().getType() == null) {
            throw new Exception("Syntax error: Unexpected end of input");
        } else switch (tokens.element().getType()) {
            case LPAREN -> {
                match(tokens.element(), Types.LPAREN);
                Node expr = expression();
                match(tokens.element(), Types.RPAREN);
                return expr;
            }
            case SUBTRACTION -> {
                match(tokens.element(), Types.SUBTRACTION);
                Node expr = expression();
                return new UnaryExpressionNode(Types.SUBTRACTION, expr);
            }
            case INTLIT -> {
                int value = Integer.parseInt(tokens.element().getLexeme());
                match(tokens.element(), Types.INTLIT);
                return new NumberNode(value);
            }
            case ID -> {
                String id = tokens.element().getLexeme();
                match(tokens.element(), Types.ID);
                String value = memory.fetch(id);
                if (value == null) {
                    throw new Exception("Variable '" + id + "' is used but not initialized.");
                }
                return new NumberNode(Integer.parseInt(value));
            }
            default -> throw new Exception("Syntax error: Unexpected token " + tokens.element().getType());
        }
    }

    public boolean match(Token token, Types expected) throws InvalidTokenException {
        if (token.getType() == expected) {
            if (expected == Types.LPAREN) {
                parenthesis.push(token);
            }
            if (expected == Types.RPAREN) {
                parenthesis.pop();
            }
            tokens.poll();
            return true;
        }
        if (expected == Types.RPAREN && token.getType() == Types.EOS) {
            System.out.println("Unclosed left parenthesis at line " + parenthesis.peek().getRow() + " column " + parenthesis.peek().getColumn());
            point_at_error(parenthesis.pop());
        } else {
            System.out.println("Mismatch at line " + token.getRow() + " column " + token.getColumn() + " expected: " + expected + " but got " + token.getType());
        }
        throw new InvalidTokenException(token.getColumn(), token.getRow());
    }

    private void point_at_error(Token token) {
        System.out.println(src);
        String point = " ".repeat(token.getColumn()) + "^";
        System.out.println(point);
    }

    private int evaluate(Node node) throws Exception {
        ParseTree parseTree = new ParseTree(node);
        return parseTree.evaluate();
    }
}
