package texteditor.history;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

enum Action { NONE, UNDO, REDO, ADD, RESET };

public class HistoryManager implements Runnable, HistoryStateMonitorIntrface {
    
    static private final int SAVE_DELAY = 20;
    static private final int PAUSE = 10;
    
    private final HistoryClient controller;
    private final Thread thread;
    private final History history;
    
    private List<Action> actions;
    private boolean inWork;
    
    private String resetHistoryValue;
    private String addHistoryItem;

    public HistoryManager(HistoryClient controller) {
        this.controller = controller;
        thread = new Thread(this);
        history = new History();
        
        updateActions();
        
        inWork = false;
        
        thread.setDaemon(true);
    }
    
    public void start() {
        inWork = true;
        thread.start();
    }
    
    public void stop() {
        inWork = false;
    }
    
    public void undo() {
        actions.add(Action.UNDO);
    }
    
    public void redo() {
        actions.add(Action.REDO);
    }
    
    public void add(String addHistoryItem) {
        this.addHistoryItem = addHistoryItem;
        updateActions();
        actions.add(Action.ADD);
    }
    
    public void resetHistory(String resetHistoryValue) {
        this.resetHistoryValue = resetHistoryValue;
        updateActions();   
        actions.add(Action.RESET);
    }

    @Override
    public void run() {
        while (inWork) {
            synchronized(controller) {
                while(!controller.isAvailableFor(this)) {
                    if (actions.size() > 0) {
                        updateActions();
                    }
                    
                    try {
                        controller.wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(HistoryManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                controller.setOccupied(this);
                
                if (actions.size() > 0) {
                    switch (actions.get(0)) {
                        case REDO:
                            history.redo();
                            setController();
                            break;
                        case UNDO:
                            history.undo();
                            setController();
                            break;
                        case RESET:
                            history.reset(resetHistoryValue);
                            controller.notifyUpdatedHistory();
                            break;
                        case ADD:
                            history.add(addHistoryItem);
                            controller.notifyUpdatedHistory();
                            break;
                    }
                    
                    actions.remove(0);
                }
                
                controller.setFree();
                controller.notifyAll();
            }
            
            try {
                Thread.sleep(PAUSE);
            } catch (InterruptedException ex) {
                Logger.getLogger(HistoryManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }     
    }

    @Override
    public String getCurrentText() {
        return history.getCurrentText();
    }

    @Override
    public int getCurrentAnchor() {
        return history.getCurrentAnchor();
    }

    @Override
    public int getCurrentIndex() {
        return history.getCurrentIndex();
    }

    @Override
    public String get(int index) {
        return history.get(index);
    }

    @Override
    public int size() {
        return history.size();
    }
    
    private void updateActions() {
        actions = new LinkedList<>();
    }
    
    private void setController() {
        controller.setState(history.getCurrentText(), history.getCurrentAnchor());
    }
}
