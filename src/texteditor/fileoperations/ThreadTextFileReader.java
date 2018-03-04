package texteditor.fileoperations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import texteditor.monitor.Monitor;

class ThreadTextFileReader implements Runnable {
    
    private final File file;
    private final Monitor controler;
    private final Thread thread;
    
    private ProcessState state;
    private StringBuilder textBuilder;

    public ThreadTextFileReader(File file, Monitor controler) {
        this.file = file;
        this.controler = controler;
        
        state = ProcessState.WAITING;
        
        thread = new Thread(this);
    }
    
    public ProcessState getState() {
        return state;
    }
    
    public void start() {
        textBuilder = new StringBuilder("");
        state = ProcessState.IN_PROGRESS;
        thread.start();
    }
    
    public void join() throws InterruptedException {
        thread.join();
    }
    
    public String getText() {
        return state == ProcessState.SUCCESS && textBuilder != null ? textBuilder.toString() : null;
    }

    @Override
    public void run() {
        synchronized(controler) {
            while(!controler.isAvailableFor(this)) {
                try {
                    controler.wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger(ThreadTextFileReader.class.getName()).log(Level.SEVERE, null, ex);
                }    
            }
            
            controler.setOccupied(this);
                
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), FileOperationData.DEFAULT_ENCODING))) {
                String line;

                while((line = reader.readLine()) != null) {
                    textBuilder.append(line).append("\n");
                }
                state = ProcessState.SUCCESS;
            } catch (FileNotFoundException ex) {
                state = ProcessState.FAILED;
                Logger.getLogger(ThreadTextFileReader.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedEncodingException ex) {
                state = ProcessState.FAILED;
                Logger.getLogger(ThreadTextFileReader.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                state = ProcessState.FAILED;
                Logger.getLogger(ThreadTextFileReader.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                controler.setFree();
                controler.notifyAll();
            }          
        }
    }
    
    
    
}
