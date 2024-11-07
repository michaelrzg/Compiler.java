/**
 * ParseTree class represents a parse tree with an evaluation method.
 * It takes the root node (an expression) and evaluates the expression's value.
 */
class ParseTree {
    private final Node root;

    /**
     * Constructor for ParseTree.
     *
     * @param root The root node of the parse tree (an expression node).
     */
    public ParseTree(Node root) {
        this.root = root;
    }

    /**
     * Evaluates the parse tree from the root node.
     *
     * @return The value of the evaluated expression.
     * @throws Exception If there is an issue during evaluation.
     */
    public int evaluate() throws Exception {
        return evaluateNode(root);
    }

    /**
     * Recursively evaluates a given node.
     *
     * @param node The node to evaluate.
     * @return The evaluated integer value of the node.
     * @throws Exception If an unsupported node type is encountered.
     */
    private int evaluateNode(Node node) throws Exception {
        if (node instanceof NumberNode) {
            return ((NumberNode) node).value;
        } else if (node instanceof UnaryExpressionNode) {
            UnaryExpressionNode unaryNode = (UnaryExpressionNode) node;
            int operandValue = evaluateNode(unaryNode.operand);

            // Handle unary operations (only subtraction in this case)
            if (unaryNode.operator == Types.SUBTRACTION) {
                return -operandValue;
            } else {
                throw new Exception("Unsupported unary operator: " + unaryNode.operator);
            }
        } else if (node instanceof BinaryExpressionNode) {
            BinaryExpressionNode binaryNode = (BinaryExpressionNode) node;
            int leftValue = evaluateNode(binaryNode.left);
            int rightValue = evaluateNode(binaryNode.right);

            // Handle binary operations: addition, subtraction, multiplication, division
            return switch (binaryNode.operator) {
                case ADDITION -> leftValue + rightValue;
                case SUBTRACTION -> leftValue - rightValue;
                case MULTIPLICATION -> leftValue * rightValue;
                case DIVISION -> {
                    if (rightValue == 0) {
                        throw new ArithmeticException("Division by zero");
                    }
                    yield leftValue / rightValue;
                }
                default -> throw new Exception("Unsupported binary operator: " + binaryNode.operator);
            };
        } else {
            throw new Exception("Unsupported node type: " + node.getClass().getName());
        }
    }
}
