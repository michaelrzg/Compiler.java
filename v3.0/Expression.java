abstract class Expression {
    Node nonterm1;
    Node nonterm2;
    public Expression() {
    }
    public Expression(Node expression, Node term){
        this.nonterm1=expression;
        this.nonterm2=term;
    }
}