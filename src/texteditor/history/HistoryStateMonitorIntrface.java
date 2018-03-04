package texteditor.history;

public interface HistoryStateMonitorIntrface {
    
    public String getCurrentText();
    
    public int getCurrentAnchor();
    
    public int getCurrentIndex();
    
    public String get(int index);
    
    public int size();
}
