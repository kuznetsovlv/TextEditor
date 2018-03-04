package texteditor.history;

import javafx.scene.control.IndexRange;

public interface HistoryStateMonitorIntrface {
    
    public String getCurrentText();
    
    public IndexRange getCurrentSelection();
    
    public int getCurrentIndex();
    
    public String get(int index);
    
    public int size();
}
