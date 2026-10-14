import java.util.ArrayList;

public class TextUndoBuffer {
    private ArrayList<String> history;

    public TextUndoBuffer() {
        history = new ArrayList<String>();
    }

    public void push(String addState) {
        history.add(addState);
    }

    public String pop() {
        if (history.isEmpty()) {
            return null;
        }

        return history.remove(history.size()-1);
    }

    public String peek() {
        if (history.isEmpty()) {
            return null;
        }

        return history.get(history.size()-1);
    }

    public boolean isEmpty() {
        return history.isEmpty();
    }
}