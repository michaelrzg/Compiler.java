abstract class Term extends Expression{

    Node nonterminal1;
    Node nonterminal2;
    public Term() {
    }
    public Term(Node term_node, Node factor){
        this.nonterminal1=term_node;
        this.nonterminal2=factor;
    }
}