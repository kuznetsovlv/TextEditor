package texteditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;

enum Action { NONE, UNDO, REDO, ADD };

public class HistoryManager implements Runnable {
    
    static private final int SAVE_DELAY = 20;
    
    private final MainController controller;
    private final Thread thread;   
    private Action action;
    private boolean inWork;
    private Timer timer;
    

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
    
    private void addHistory() {
        if (timer.isRunning()) {
            timer.restart();
        } else {
            timer.start();
        }
        
    }

    @Override
    public void run() {
        while (inWork) {
            synchronized(controller) {
                switch (action) {
                    case REDO:
                        controller.redoText();
                        break;
                    case UNDO:
                        controller.undoText();
                        break;
                    case ADD:
                        addHistory();
                        break;
                }
                
                action = Action.NONE;
            }
        }     
    }
    
}
