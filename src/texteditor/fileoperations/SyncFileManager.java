package texteditor.fileoperations;

import java.io.File;
import texteditor.monitor.Monitor;

public class SyncFileManager {
    private static SyncFileManager fileManager;

    private SyncFileManager() {
    }
    
    public static SyncFileManager instance() {
        if (fileManager == null) {
            fileManager = new SyncFileManager();
        }
        
        return fileManager;
    }
    
    public File writeTextFile(File file, String str, Monitor monitor) throws InterruptedException {
        ThreadTextFileWriter writer = new ThreadTextFileWriter(file, str, monitor);
        writer.start();
        writer.join();
        
        return writer.getState() == ProcessState.SUCCESS ? file : null;
    }
    
    public String readTextFile(File file, Monitor monitor) throws InterruptedException {
        ThreadTextFileReader reader = new ThreadTextFileReader(file, monitor);
        reader.start();
        reader.join();
        
        return reader.getState() == ProcessState.SUCCESS ? reader.getText() : null;
    }
}
