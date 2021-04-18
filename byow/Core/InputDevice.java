package byow.Core;

public interface InputDevice {
    boolean hasNextChar();

    char nextChar();

    void addInputs(String s);

    String getAllInputs();

    boolean inputEnded();

}
