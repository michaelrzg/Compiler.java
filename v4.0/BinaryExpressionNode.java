class BinaryExpressionNode extends Node {
    Node left;
    Types operator;
    Node right;
    
    public BinaryExpressionNode(Node left, Types operator, Node right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }
}