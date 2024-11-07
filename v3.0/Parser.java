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
    //  Stack to keep track of left parenthesis closure
    private final Stack<Token> parenthesis;
    // full src string for error pointing
    private final String src;

   /**
    * Parser verifies that the code provided is syntactically correct.
    * 
     @param allTokens  Queue<Token> of all tokens in order
     @param src  String containing all code in file

    */
    public Parser(Queue<Token> allTokens, String src)  throws InvalidTokenException{
        tokens = allTokens;
        parenthesis =new Stack<>();
        this.src = src;
        expression();
        if(tokens.size() !=1){
            System.out.println("\n\nERROR");
            point_at_error(tokens.element());
        throw new InvalidTokenException(tokens.element().getColumn(), tokens.element().getRow());

        }
        else{
            System.out.println("Parser ran with no errors found.");
        }
    }
    
    /**
    * match verifies that the token provided has the same type as the expected token.
    * 
     @param token  Token to be checked
     @param expected  Expected type 
     @return True if types match, else false

    */
    public boolean match(Token token, Types expected) throws InvalidTokenException
    {   
        if(token.getType() == expected){
            if(expected == Types.LPAREN){
                parenthesis.push(token);
            }
            if(expected == Types.RPAREN){
                parenthesis.pop();
            }
            tokens.remove();

            return true;
        }
        if(expected == Types.RPAREN && token.getType() == Types.EOS){
            System.out.println("Unclosed left parenthesis at line " + parenthesis.peek().getRow()+ " column " +parenthesis.peek().getColumn());
            point_at_error(parenthesis.pop());
        }
        else{
        System.out.println("Mismatch at line "+ token.getRow()+ " column " +token.getColumn()+ " expected: "  + expected + " but got " + token.getType());
        }
        throw new InvalidTokenException(token.getColumn(), token.getRow());
        
    }
     /**
    * Factor function that determines which set case its RHS contains and follows the expected path.
    */
    private void factor() throws InvalidTokenException{
        if(null == tokens.element().getType()){
            number();
        }
        else switch (tokens.element().getType()) {
            case LPAREN -> {
                match(tokens.element(),Types.LPAREN);
                expression();
                match(tokens.element(),Types.RPAREN);
            }
            case SUBTRACTION -> {
                match(tokens.element(),Types.SUBTRACTION);
                expression();
            }
            default -> number();
        }
    }
    /**
    * Expression function that determines which set case its RHS contains and follows the expected path.
    */
    private void expression() throws InvalidTokenException{
        term();
        expression_prime();
    }
     /**
    * Expression Prime function that determines which set case its RHS contains and follows the expected path (to prevent runnaway recursion).
    */
    private void expression_prime() throws InvalidTokenException{
         if(tokens.element().getType() == Types.ADDITION){
            match(tokens.element(),Types.ADDITION);
            term();
            expression_prime();
        }
        else if (tokens.element().getType() == Types.SUBTRACTION){
            match(tokens.element(),Types.SUBTRACTION);
            term();
            expression_prime();
        }
    }
    /**
    * Term function that determines which set case its RHS contains and follows the expected path.
    */
    private void term() throws InvalidTokenException{
        factor();
        term_prime();
    }
     /**
    * Term Prime function that determines which set case its RHS contains and follows the expected path (to prevent runnaway recursion).
    */
    private void term_prime() throws InvalidTokenException{
        if (tokens.element().getType() == Types.MULTIPLICATION){
            match(tokens.element(),Types.MULTIPLICATION);
            factor();
            term_prime();
        }
        else if (tokens.element().getType() == Types.DIVISION){
            match(tokens.element(), Types.DIVISION);
            factor();
            term_prime();
        }
    }
     /**
    * Number function that determines which set case its RHS contains and follows the expected path (either intlit or null).
    */
    private void number() throws InvalidTokenException{
        if(tokens.element().getType() == Types.INTLIT){
        match(tokens.element(), Types.INTLIT);
        }
    }
     /**
    * Function that points at where the error occures in the original code
    @param token The token causing the error
    */
    private void point_at_error(Token token){
        System.out.println(src);
        String point = "";
            for (int i=0;i<token.getColumn();i++){
                point+=" ";
            }
            point+="^";
            System.out.println(point);
    }

    public Node getExpressionNode() throws Exception {
        Node termNode = parseTerm();
        return getExpressionPrimeNode(termNode);
    }

    private Node getExpressionPrimeNode(Node expr) throws Exception {
        if (tokens.element().getType() == Types.ADDITION) {
            match(tokens.element(),Types.ADDITION);
            Node termNode = parseTerm();
            Node expr1 = new BinaryExpressionNode(expr, Types.ADDITION, termNode);
            return getExpressionPrimeNode(expr1);
        } else if (tokens.element().getType() == Types.SUBTRACTION) {
            match(tokens.element(),Types.SUBTRACTION);
            Node termNode = parseTerm();
            Node expr1 = new BinaryExpressionNode(expr, Types.SUBTRACTION, termNode);
            return getExpressionPrimeNode(expr1);
        } else {
            return expr;
        }
    }

    private Node parseTerm() throws Exception {
        Node factorNode = parseFactor();
        return parseTermPrime(factorNode);
    }

    private Node parseTermPrime(Node term) throws Exception {
        if (tokens.element().getType() == Types.MULTIPLICATION) {
            match(tokens.element(),Types.MULTIPLICATION);
            Node factorNode = parseFactor();
            Node term1 = new BinaryExpressionNode(term, Types.MULTIPLICATION, factorNode);
            return parseTermPrime(term1);
        } else if (tokens.element().getType() == Types.DIVISION) {
            match(tokens.element(),Types.DIVISION);
            Node factorNode = parseFactor();
            Node term1 = new BinaryExpressionNode(term, Types.DIVISION, factorNode);
            return parseTermPrime(term1);
        } else {
            return term;
        }
    }

    private Node parseFactor() throws Exception {
        if (tokens.element().getType() == Types.LPAREN) {
            match(tokens.element(),Types.LPAREN);
            Node expr = getExpressionNode();
            match(tokens.element(),Types.RPAREN);
            return expr;
        } else if (tokens.element().getType() == Types.SUBTRACTION) {
            match(tokens.element(),Types.SUBTRACTION);
            Node expr = getExpressionNode();
            return new UnaryExpressionNode(Types.SUBTRACTION, expr);
        } else if (tokens.element().getType() == Types.INTLIT) {
            int value = Integer.parseInt(tokens.element()+"");
            match(tokens.element(),Types.INTLIT);
            return new NumberNode(value);
        } else {
            throw new Exception("Syntax error: Unexpected token " + tokens.element().getType());
        }
    }

}