package texteditor.history;

import javafx.scene.control.IndexRange;

public interface HistoryStateMonitorIntrface {
    
    public String getCurrentText();
    
    public int getCurrentAnchor();
    
    public int getCurrentIndex();
    
    public String get(int index);
    
    public int size();
}
