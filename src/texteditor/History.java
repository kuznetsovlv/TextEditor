package texteditor;

import java.util.LinkedList;
import java.util.List;

public class History {
    private List<String> historyList;
    private int index;

    public History(String initialHistory) {
        historyList = new LinkedList<>();
        historyList.add(initialHistory);
        index = 0;
    }

    public History() {
        this("");
    }
    
    public String getCurrent() {
        return historyList.get(index);
    }
    
    public int getCurrentIndex() {
        return index; 
    }
    
    public String get(int position) {
        if(position < 0 || position >= historyList.size()) {
            return null;
        }
        
        return historyList.get(position);
    }
    
    public int size() {
        return historyList.size();
    }
    
    public void add(String str) {
        cut();
        historyList.add(str);
        ++index;
    }
    
    public void undo(int shift) {
        shiftIndex(-shift);
    }
    
    public void undo() {
        undo(1);
    }
    
    public void redo(int shift) {
        shiftIndex(shift);
    }
    
    public void redo() {
        redo(1);
    }
    
    private void setIndex (int index) {
        this.index = index < 0 ? 0 : index >= historyList.size() ? historyList.size() - 1 : index;
    }
    
    private void shiftIndex(int shift) {
        setIndex(index + shift);
    }
    
    private void cut() {
        if (index < historyList.size() - 1) {
            List<String> oldHistoryList = historyList;
            historyList = new LinkedList<>();
            
            for (int i = 0; i <= index; ++i) {
                historyList.add(oldHistoryList.get(i));
            }
        }
    }
    
}
