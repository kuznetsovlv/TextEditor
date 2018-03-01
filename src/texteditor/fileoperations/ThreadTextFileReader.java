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
import texteditor.Callback;
import texteditor.monitor.Monitor;

public class ThreadTextFileReader implements Runnable {
    
    private final File file;
    private final Monitor controler;
    private final Callback callback;

    public ThreadTextFileReader(File file, Monitor controler, Callback callback) {
        this.file = file;
        this.controler = controler;
        this.callback = callback;
        
        new Thread(this).start();
    }
    
    

    public ThreadTextFileReader(File file, Monitor controler) {
        this(file, controler, null);
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
                StringBuilder strBuld = new StringBuilder();

                while((line = reader.readLine()) != null) {
                    strBuld.append(line).append("\n");
                }

//                controler.resetHistory(strBuld.toString());
                
                if (callback != null) {
                    callback.call();
                }

            } catch (FileNotFoundException ex) {
                Logger.getLogger(ThreadTextFileReader.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(ThreadTextFileReader.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ThreadTextFileReader.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                controler.setFree();
                controler.notifyAll();
            }          
        }
    }
    
    
    
}
