class VariableNode extends Node {
    private final String identifier;

    public VariableNode(String identifier) {
        this.identifier = identifier;
    }


    public String getIdentifier() {
        return identifier;
    }
}
