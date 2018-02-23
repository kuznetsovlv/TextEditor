package texteditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;

enum Action { NONE, UNDO, REDO, ADD, RESET };

public class HistoryManager implements Runnable {
    
    static private final int SAVE_DELAY = 20;
    
    private final MainController controller;
    private final Thread thread;   
    private Action action;
    private boolean inWork;
    private Timer timer;
    private String resetHistoriValue;
    

    public HistoryManager(MainController controller) {
        this.controller = controller;
        thread = new Thread(this);
        action = Action.NONE;
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
        action = Action.UNDO;
    }
    
    public void redo() {
        action = Action.REDO;
    }
    
    public void add() {
        action = Action.ADD;
    }
    
    public void resetHistory(String resetHistoriValue) {
        this.resetHistoriValue = resetHistoriValue;
        action = Action.RESET;
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
                while(!controller.isFree()) {
                    action = Action.NONE;
                    
                    try {
                        controller.wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(HistoryManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                controller.setOccupied();
                
                switch (action) {
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

                action = Action.NONE;
                
                controller.setFree();
                controller.notifyAll();
            }
        }     
    }
    
}
