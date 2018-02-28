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

class ThreadFileWriter implements Runnable{
    private final File file;
    private final MainController controler;
    private final Thread thread;
    private ProcessState state;

    public ThreadFileWriter(File file, MainController controler) {
        this.file = file;
        this.controler = controler;
        
        state = ProcessState.WAITING;
        
        thread = new Thread(this);
    }
    
    public ProcessState getState() {
        return state;
    }
    
    public void join() throws InterruptedException {
        thread.join();
    }
    
    public void start() {
        state = ProcessState.IN_PROGRESS;
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
                
            } catch (FileNotFoundException ex) {
                stop(ProcessState.FAILED);
                Logger.getLogger(ThreadFileWriter.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedEncodingException ex) {
                stop(ProcessState.FAILED);
                Logger.getLogger(ThreadFileWriter.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                stop(ProcessState.FAILED);
                Logger.getLogger(ThreadFileWriter.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                controler.setFree();
                controler.notifyAll();
                stop();
            }
        }
    }
    
    private void stop() {
        stop(ProcessState.SUCCESS);
    }
    
    private void stop(ProcessState state) {
        if(this.state != ProcessState.IN_PROGRESS) {
            return;
        }
        
        this.state = state != null ? state : ProcessState.STOPPED;
    }
}
