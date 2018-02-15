package texteditor;

import java.util.LinkedList;
import java.util.List;

public class History {
    
    private List<String> history;
    private int index;

    public History(String str) {
        history = new LinkedList<>();
        history.add(str);
        index = 0;
    }

    public History() {
        this("");
    }
    
    public int size() {
        return history.size();
    }
    
    public String getCurrent() {
        return history.get(index);
    }
    
    public int getCurrentIndex() {
        return index;
    }
    
    public String get(int number) {
        if (number < 0 || number >= history.size()) {
            return null;
        }
        
        return history.get(number);
    }
    
    private void move(int shift) {
        index += shift;
        
        if (index < 0) {
            index = 0;
        } else if (index >= history.size()) {
            index = history.size() - 1;
        }
    }
    
    public void undo() {
        if (index > 0) {
            --index;
        }
    }
    
    public void undo(int shift) {
        move(-shift);
    }
    
    public void redo() {
        if (index < history.size() - 1) {
            ++index;
        }
    }
    
    public void redo(int shift) {
        move(shift);
    }
    
    private void coppyHistory() {
        if (index == history.size() - 1) {
            return;
        }
        
        List<String> newHistory = new LinkedList<>();
        
        for(int i = 0; i <= index; ++i) {
            newHistory.add(history.get(i));
        }
        
        history = newHistory;
    }
    
    public void add(String str) {
        coppyHistory();
        history.add(str);
        ++index;
    }
}

