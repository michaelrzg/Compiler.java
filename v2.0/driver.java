// Name: Michael Rizig
// Class: CS 4308/W01
// Term: Fall 2024
// Instructor: Gayler
// Project Part 1
// File: Driver

//this is the driver for the Lexical Analyzer of a simple calculator
import java.io.File;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
class driver {

    public static void main(String[] args) {
        // make user pass sorce code as argument txt file path
        try {
            if (args.length == 0) {
                throw new InvalidSourceCode();
            }
        } catch (InvalidSourceCode e) {
            e.printStackTrace();
            return;
        }
        //create string
        String src = "";
        //parse file and add all characters to string
        try {
            File f = new File(args[0]);
            Scanner sc = new Scanner(f);
            src += sc.nextLine();
            sc.close();
        } catch (Exception e) {
            System.err.println("FILE '" + args[0]+"' NOT FOUND OR IS NOT READABLE");
            System.exit(1);
        }
        System.out.println(src);
        // pass string to Lexical Analyzer object
        LexicalAnalyzer l = new LexicalAnalyzer(src);
        Queue<Token> allTokens = new LinkedList<>();

        // pointer
        Token current = new Token();
        
        while (current.getType() != Types.EOS) {
            try {
                current = l.getToken();
                allTokens.add(current);
                

            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
            if (current.getType() != Types.EOS) {
                System.out.println(current);
            }
            else if ((current.getType() == Types.EOS)) 
            {System.out.println("EOS Token");}

        }
        // merge int tokens that are adjacent
        // token (3) followed by token (4) should be token (34)
        Queue<Token> merged = mergeIntTokens(allTokens);

        // at this point all vaid tokens are inside the allTokens arraylist
        try{
        Parser parser = new Parser(merged,src);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

     /**
    * Merge adjacent integer literal tokens into one token.
    @param tokens Queue<Token> containing all tokens in order
    @return Queue<Token> containing all tokens with int literal values combined
    */
    public static Queue<Token> mergeIntTokens(Queue<Token> tokens){
        Stack<Token> stack = new Stack<>();
        Queue<Token> output = new LinkedList<>();
        for(Token x : tokens){
            if(x.getType() == Types.INTLIT ){
                if(stack.isEmpty()){
                stack.push(x);
                }
                else {
                    Token temp = stack.pop();
                    String new_value = temp.getLexeme() + "" + x.getLexeme();
                    stack.push(new Token(new_value,temp.getType(),temp.getColumn(), temp.getRow()));
                    
                }
            }
            else{
                if(stack.isEmpty()){
                    output.add(x);
                }
                else{
                    output.add(stack.pop());
                    output.add(x);
                }
            }

        }
        return output;

    }
}
