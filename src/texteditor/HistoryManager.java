package texteditor;

public class HistoryMover implements Runnable {
    
    private final MainController controller;
    private final Thread thread;
    private int direction;

    public HistoryMover(MainController controller, int direction) {
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
            if (direction > 0) {
                controller.redoHistory();
            } else {
                controller.undoHistory();
            }
        }
    }
    
}
