package texteditor;

import java.util.LinkedList;
import java.util.List;
import javafx.scene.control.IndexRange;

public class History {
    
    private class Item {
        String text;
        IndexRange selection;

        public Item(String text, IndexRange selection) {
            this.text = text;
            this.selection = selection;
        }
        
        public String getText() {
            return text;
        }

        public IndexRange getSelection() {
            return selection;
        }
    }
    
    private List<Item> historyList;
    private int index;

    public History(String initialHistory) {
        reset(initialHistory);
    }

    public History() {
        this("");
    }
    
    public String getCurrent() {
        return historyList.get(index).getText();
    }
    
    public IndexRange getCurrentSelection() {
        return historyList.get(index).getSelection();
    }
    
    public int getCurrentIndex() {
        return index; 
    }
    
    public String get(int position) {
        if(position < 0 || position >= historyList.size()) {
            return null;
        }
        
        return historyList.get(position).getText();
    }
    
    public int size() {
        return historyList.size();
    }
    
    public void add(String str, IndexRange selection) {
        cut();
        historyList.add(new Item(str, selection));
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
    
   public void reset() {
       reset("");
   }
    
    public void reset(String initialHistory) {
        historyList = new LinkedList<>();
        historyList.add(new Item(initialHistory, new IndexRange(0, 0)));
        index = 0;
    }
    
    private void setIndex (int index) {
        this.index = index < 0 ? 0 : index >= historyList.size() ? historyList.size() - 1 : index;
    }
    
    private void shiftIndex(int shift) {
        setIndex(index + shift);
    }
    
    private void cut() {
        if (index < historyList.size() - 1) {
            List<Item> oldHistoryList = historyList;
            historyList = new LinkedList<>();
            
            for (int i = 0; i <= index; ++i) {
                historyList.add(oldHistoryList.get(i));
            }
        }
    }
    
}
