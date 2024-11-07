class UnaryExpressionNode extends Node {
    Types operator;
    Node operand;
    
    public UnaryExpressionNode(Types operator, Node operand) {
        this.operator = operator;
        this.operand = operand;
    }
}