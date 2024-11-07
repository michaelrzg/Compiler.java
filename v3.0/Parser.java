// Name: Michael Rizig
// Class: CS 4308/W01
// Term: Fall 2024
// Instructor: Gayler
// Project Part 2
// File: Parser
import java.util.Queue;
import java.util.Stack;
class Parser {

    // Queue to hold tokens
    private final Queue<Token> tokens;
    // Stack to keep track of left parenthesis closure
    private final Stack<Token> parenthesis;
    // Full source string for error pointing
    private final String src;

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
        
        // Parse the expression
        Node root = expression();

        // Check if there are extra tokens left
        if (tokens.size() != 1) {
            System.out.println("\n\nERROR");
            point_at_error(tokens.element());
            throw new InvalidTokenException(tokens.element().getColumn(), tokens.element().getRow());
        } else {
            System.out.println("Parser ran with no errors found.");
        }

        // Create ParseTree and evaluate
        ParseTree parseTree = new ParseTree(root);
        int result = parseTree.evaluate();
        System.out.println("The result of the expression is: " + result);
    }

    public boolean match(Token token, Types expected) throws InvalidTokenException {
        if (token.getType() == expected) {
            if (expected == Types.LPAREN) {
                parenthesis.push(token);
            }
            if (expected == Types.RPAREN) {
                parenthesis.pop();
            }
            tokens.remove();
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

    public Node expression() throws Exception {
        Node termNode = term();
        return expression_prime(termNode);
    }

    private Node expression_prime(Node expr) throws Exception {
        if (tokens.element().getType() == Types.ADDITION) {
            match(tokens.element(), Types.ADDITION);
            Node termNode = term();
            Node expr1 = new BinaryExpressionNode(expr, Types.ADDITION, termNode);
            return expression_prime(expr1);
        } else if (tokens.element().getType() == Types.SUBTRACTION) {
            match(tokens.element(), Types.SUBTRACTION);
            Node termNode = term();
            Node expr1 = new BinaryExpressionNode(expr, Types.SUBTRACTION, termNode);
            return expression_prime(expr1);
        } else {
            return expr;
        }
    }

    private Node term() throws Exception {
        Node factorNode = factor();
        return term_prime(factorNode);
    }

    private Node term_prime(Node term) throws Exception {
        if (tokens.element().getType() == Types.MULTIPLICATION) {
            match(tokens.element(), Types.MULTIPLICATION);
            Node factorNode = factor();
            Node term1 = new BinaryExpressionNode(term, Types.MULTIPLICATION, factorNode);
            return term_prime(term1);
        } else if (tokens.element().getType() == Types.DIVISION) {
            match(tokens.element(), Types.DIVISION);
            Node factorNode = factor();
            Node term1 = new BinaryExpressionNode(term, Types.DIVISION, factorNode);
            return term_prime(term1);
        } else {
            return term;
        }
    }

    private Node factor() throws Exception {
        if (tokens.element().getType() == Types.LPAREN) {
            match(tokens.element(), Types.LPAREN);
            Node expr = expression();
            match(tokens.element(), Types.RPAREN);
            return expr;
        } else if (tokens.element().getType() == Types.SUBTRACTION) {
            match(tokens.element(), Types.SUBTRACTION);
            Node expr = expression();
            return new UnaryExpressionNode(Types.SUBTRACTION, expr);
        } else if (tokens.element().getType() == Types.INTLIT) {
            int value = Integer.parseInt(tokens.element().getLexeme()+""); // Assuming token has getValue() for int literals
            match(tokens.element(), Types.INTLIT);
            return new NumberNode(value);
        } else {
            throw new Exception("Syntax error: Unexpected token " + tokens.element().getType());
        }
    }
}
