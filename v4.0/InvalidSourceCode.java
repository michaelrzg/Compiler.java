public class InvalidSourceCode extends Exception {
    public InvalidSourceCode() {
        super("Invalid Usage. \n Usage:\n java driver.java [path to your source code here]");
    }
}
