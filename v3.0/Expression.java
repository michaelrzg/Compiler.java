abstract class Expression extends Node{
    Node term;
    Node expression_prime;
    public Expression(Node term, Node expression_prime){
        this.term = term;
        this.expression_prime = expression_prime;
    }
    public Expression(){}
}