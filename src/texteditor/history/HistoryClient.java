package texteditor.history;

import texteditor.monitor.Monitor;

public interface HistoryClient extends Monitor {
    public void setState(String text, int anchorn);
    public void notifyUpdatedHistory();
}
