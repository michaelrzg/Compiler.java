abstract class Term extends Expression{
    Node factor;
    Node term_prime;

    public Term(Node factor, Node tern_prime) {
        this.factor = factor;
        this.term_prime=tern_prime;
    
    }

    
}