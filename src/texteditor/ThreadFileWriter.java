package texteditor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadFileWriter implements Runnable{
    private final File file;
    private final MainController controler;
    private final Callback callback;
    
    private final Thread thread;

    public ThreadFileWriter(File file, MainController controler, Callback callback) {
        this.file = file;
        this.controler = controler;
        this.callback = callback;
        
        thread = new Thread(this);
    }
    
    public ThreadFileWriter(File file, MainController controler) {
        this(file, controler, null);
    }
    
    public void join() throws InterruptedException {
        thread.join();
    }
    
    public void start() {
        thread.start();
    }

    @Override
    public void run() {
        synchronized(controler) {
            while(!controler.isAvailableFor(this)) {
                try {
                    controler.wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger(ThreadFileWriter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            controler.setOccupied(this);
            
            try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), FileOperationData.DEFAULT_ENCODING))) {
                writer.write(controler.getText());
                controler.setDataUnsaved(false);
                
                if (callback != null) {
                    callback.call();
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ThreadFileWriter.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(ThreadFileWriter.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ThreadFileWriter.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                controler.setFree();
                controler.notifyAll();
            }
        }
    }
    
    
}
