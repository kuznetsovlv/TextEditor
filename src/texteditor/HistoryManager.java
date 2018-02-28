package texteditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;

enum Action { NONE, UNDO, REDO, ADD, RESET };

public class HistoryManager implements Runnable {
    
    static private final int SAVE_DELAY = 20;
    
    private final MainController controller;
    private final Thread thread;   
    private List<Action> actions;
    private boolean inWork;
    private Timer timer;
    private String resetHistoriValue;
    

    public HistoryManager(MainController controller) {
        this.controller = controller;
        thread = new Thread(this);
        updateActions();
        inWork = false;
        timer = new Timer(SAVE_DELAY, (ActionEvent e) -> {
            controller.addHistory();
            timer.stop();
        });
        
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
    
    public void add() {
        actions.add(Action.ADD);
    }
    
    public void resetHistory(String resetHistoriValue) {
        this.resetHistoriValue = resetHistoriValue;
        updateActions();
        actions.add(Action.RESET);
    }
    
    private void updateActions() {
        actions = new LinkedList<>();
    }
     
    private void addHistory() {
        if (timer.isRunning()) {
            timer.restart();
        } else {
            timer.start();
        }
    }
    
    
    private void stopTimer() {
        if(timer.isRunning()) {
            timer.stop();
        }
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
                
                if (actions.size() > 0) {
                    controller.setOccupied(this);
                    
                    switch (actions.get(0)) {
                        case REDO:
                            stopTimer();
                            controller.redoText();
                            break;
                        case UNDO:
                            stopTimer();
                            controller.undoText();
                            break;
                        case RESET:
                            stopTimer();
                            controller.resetHistory(resetHistoriValue);
                        case ADD:
                            addHistory();
                            break;
                    }
                    
                    actions.remove(0);
                    controller.setFree();
                }
                
                controller.notifyAll();
            }
        }     
    }
    
}
