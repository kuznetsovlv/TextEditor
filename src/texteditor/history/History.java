package texteditor.history;

import java.util.LinkedList;
import java.util.List;

class History implements HistoryStateMonitorIntrface {    
    private List<Item> historyList;
    private int index;

    public History(String initialHistory) {
        reset(initialHistory);
    }

    public History() {
        this("");
    }
    
    @Override
    public String getCurrentText() {
        return historyList.get(index).getText();
    }
    
    @Override
    public int getCurrentAnchor() {
        return historyList.get(index).getAnchor();
    }
    
    @Override
    public int getCurrentIndex() {
        return index; 
    }
    
    @Override
    public String get(int position) {
        if(position < 0 || position >= historyList.size()) {
            return null;
        }
        
        return historyList.get(position).getText();
    }
    
    @Override
    public int size() {
        return historyList.size();
    }
    
    public void add(Item historyItem) {
        if (historyItem.getText().equals(getCurrentText())) {
            return;
        }
        
        cut();
        historyList.add(historyItem);
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
        historyList.add(new Item(initialHistory, 0));
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
