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

    public ThreadFileWriter(File file, MainController controler) {
        this.file = file;
        this.controler = controler;
        
        new Thread(this).start();
    }

    @Override
    public void run() {
        synchronized(controler) {
            while(!controler.isFree()) {
                try {
                    controler.wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger(ThreadFileWriter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            controler.setOccupied();
            
            try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), FileOperationData.DEFAULT_ENCODING))) {
                writer.write(controler.getText());
                controler.setDataUnsaved(false);
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
