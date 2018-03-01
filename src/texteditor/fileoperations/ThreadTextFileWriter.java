package texteditor.fileoperations;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import texteditor.monitor.Monitor;

class ThreadTextFileWriter implements Runnable{
    private final File file;
    private final Monitor controler;
    private final Thread thread;
    private final String savingText;
    private ProcessState state;

    public ThreadTextFileWriter(File file, String savingText, Monitor controler) {
        this.file = file;
        this.savingText = savingText;
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
                    Logger.getLogger(ThreadTextFileWriter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            controler.setOccupied(this);
            
            try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), FileOperationData.DEFAULT_ENCODING))) {
                writer.write(savingText);
                
            } catch (FileNotFoundException ex) {
                stop(ProcessState.FAILED);
                Logger.getLogger(ThreadTextFileWriter.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedEncodingException ex) {
                stop(ProcessState.FAILED);
                Logger.getLogger(ThreadTextFileWriter.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                stop(ProcessState.FAILED);
                Logger.getLogger(ThreadTextFileWriter.class.getName()).log(Level.SEVERE, null, ex);
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
