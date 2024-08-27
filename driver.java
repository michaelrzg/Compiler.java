// Name: Michael Rizig
// Class: CS 4308/W01
// Term: Fall 2024
// Instructor: Gayler
// Project Part 1
// File: Driver

//this is the driver for the Lexical Analyzer of a simple calculator
import java.util.Scanner;
import java.io.File;

class driver {
    public static void main(String[] args) {
        // make user pass sorce code as argument txt file path
        try {
            if (args.length == 0) {
                throw new InvalidSourceCode();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        String src = "";
        try {
            File f = new File(args[0]);
            Scanner sc = new Scanner(f);
            src = sc.nextLine();
            sc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(src);

        LexicalAnalyzer l = new LexicalAnalyzer(src);
        Token current = new Token();
        while (current.getType() != Types.EOS) {
            try {
                current = l.getToken();
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
            if (current.getType() != Types.EOS) {
                System.out.println(current);
            }

        }
    }
}
