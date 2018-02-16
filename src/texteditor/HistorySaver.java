package texteditor;

import java.util.logging.Level;
import java.util.logging.Logger;


public class HistorySaver implements Runnable {

    private final long PAUSE = 500;
    private final MainController controller;
    private final Thread thread;

    public HistorySaver(MainController controller) {
        this.controller = controller;
        thread = new Thread(this);
    }
    
    public void start() {
        thread.start();
    }
    
    @Override
    public void run() {
        try {
            Thread.sleep(PAUSE);
        } catch (InterruptedException ex) {
            Logger.getLogger(HistorySaver.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        synchronized(controller) {
            controller.addHistory();
        }
    }
    
}
