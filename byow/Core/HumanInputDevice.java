package byow.Core;

import edu.princeton.cs.introcs.StdDraw;

public class HumanInputDevice implements InputDevice {
    private String buffer = "";
    private String allInputs = "";

    public boolean hasNextChar() {
        return buffer.length() > 0 || StdDraw.hasNextKeyTyped();
    }

    public char nextChar() {
        char c = 'u';
        if (buffer.length() > 0) {
            c = buffer.charAt(0);
            buffer = buffer.substring(1);

        } else {
            c = StdDraw.nextKeyTyped();
        }
        if (c != 'l' && c != 'q' && c != ':') {
            allInputs += Character.toString(c);
        }

        return c;
    }

    public void addInputs(String s) {
        buffer += s;
    }

    @Override
    public String getAllInputs() {
        return allInputs;
    }

    public boolean inputEnded(){
        return false;
    }
}
