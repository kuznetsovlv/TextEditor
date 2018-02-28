package texteditor;

import java.io.File;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

class DialogManager {
    
    private static DialogManager dialogManager;
    private Stage stage;
    private MainController controller;

    private DialogManager() {
        dialogManager = this;
    }

    private DialogManager(Stage stage) {
        this();
        setStage(stage);
    }
    
    
    public static DialogManager instance(Stage stage) {
        if (dialogManager == null) {
            dialogManager = new DialogManager(stage);
        } else {
            dialogManager.setStage(stage);
        }
        
        return dialogManager;
    }
    
    public static DialogManager instance() {
        if(dialogManager == null) {
            dialogManager = new DialogManager();
        }
        
        return dialogManager;
    }
    
    public void setController(MainController controller) {
        this.controller = controller;
    }
    
    public File saveByDialog(File file) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save to");
        fileChooser.setInitialDirectory(new File(file != null ? file.getParent() : System.getenv("PWD")));
        
        if (file != null) {
            fileChooser.setInitialFileName(file.getAbsolutePath());
        }
        
        file = fileChooser.showSaveDialog(stage);
        
        return file != null ? saveFile(file) : null;
    }
    
    public File saveFile(File file) {
        if (file == null) {
            file = saveByDialog(null);
        } else {
            try {
                ThreadFileWriter writer = new ThreadFileWriter(file, controller);
                writer.start();
                writer.join();
                if (writer.getState() != ProcessState.SUCCESS) {
                    file = null;
                }
            } catch(Exception e) {
                file = null;
            }
        }
        
        return file;
    }
    
    public void setMainTitle (String title) {
        if (stage != null) {
            stage.setTitle(title != null ? title : TextEditor.DEFAULT_TITLE);
        }
    }
    
    private void setStage(Stage stage) {
        this.stage = stage;
    }
    
}
