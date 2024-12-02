abstract class Factor extends Term {
    Node nonterm1;
    Node terminal;
    public Factor() {
    }
    public Factor(Node terminal, Node nonterminal){
        this.nonterm1 = nonterminal;
        this.terminal=terminal;
    }
}
