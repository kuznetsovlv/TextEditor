package texteditor;

import java.util.logging.Level;
import java.util.logging.Logger;

public class HistoryManager implements Runnable {
    
    private final MainController controller;
    private final Thread thread;
    private int direction;

    public HistoryManager(MainController controller, int direction) {
        this.controller = controller;
        this.direction = direction;
        thread = new Thread(this);
    }
    
    public void start() {
        thread.start();
    }

    @Override
    public void run() {
        synchronized(controller) {
            while(!controller.isFree()) {
                try {
                    controller.wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger(HistoryManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            controller.setOccupied();
            
            if (direction > 0) {
                controller.redoText();
            } else if (direction < 0) {
                controller.undoText();
            }
            
            controller.enableHistoryItems();
            controller.setFree();
            controller.notifyAll();
        }
    }
    
}
