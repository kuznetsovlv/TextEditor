package texteditor.windows;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class DialogManager {
    
    private static DialogManager dialogManager;
    private Stage stage;
    private String defaultTitle;

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
    
    public void start() throws IOException {
        if (stage == null) {
            return;
        }
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TextEditorPane.fxml"));
        
        Parent root = loader.load();
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setOnCloseRequest((WindowEvent event) -> {
            event.consume();
            MainController controller = (MainController) loader.getController();
            controller.exit();
        });
        stage.show();
    }
    
    public File openSaveDialog(File file) {
        FileChooser fileChooser = createFileChooser(file, "Save to");
        
        if (file != null) {
            fileChooser.setInitialFileName(file.getAbsolutePath());
        }
        
        return fileChooser.showSaveDialog(stage);
    }
    
    public File openChoseDialog(File file) {
        FileChooser fileChooser = createFileChooser(file, "Select file");
        
        return fileChooser.showOpenDialog(stage);
    }
    
    public void openUnsavedDataDialog(DialogReaction reaction) {
        try {
            new DialogCreator(reaction,
                    "You have unsaved data which will be lost after next operation. Would you like to save it before continuos?"
                    , "Unsaved data");
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void showAboutWindow() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("AboutPane.fxml"));
        
        Scene scene = new Scene(root);
        
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("About");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    
    public void setDefaultTitle(String defaultTitle) {
        this.defaultTitle = defaultTitle;
    }
    
    public void setMainTitle (String title) {
        if (stage != null) {
            stage.setTitle(title != null ? title : defaultTitle != null ? defaultTitle : "");
        }
    }
    
    private void setStage(Stage stage) {
        this.stage = stage;
    }
    
    private FileChooser createFileChooser(File file, String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.setInitialDirectory(new File(file != null ? file.getParent() : System.getenv("PWD")));
        
        return fileChooser;
    }
}
