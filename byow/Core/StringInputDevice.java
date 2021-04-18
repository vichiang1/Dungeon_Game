package byow.Core;

import edu.princeton.cs.introcs.StdDraw;

public class StringInputDevice implements InputDevice {
    String buffer = "";
    String allInputs = "";


    @Override
    public boolean hasNextChar() {
        return buffer.length() > 0;
    }

    @Override
    public char nextChar() {
        char c = buffer.charAt(0);
        buffer = buffer.substring(1);
        if (c != 'l' && c != 'q' && c != ':') {
            allInputs += Character.toString(c);
        }
        return c;
    }

    @Override
    public void addInputs(String s) {
        buffer  = s + buffer;
    }

    @Override
    public String getAllInputs() {
        return allInputs;
    }

    @Override
    public boolean inputEnded() {
        return !hasNextChar();
    }
}
