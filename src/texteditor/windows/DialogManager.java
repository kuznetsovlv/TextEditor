package texteditor.windows;

import java.io.File;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
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
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save to");
        fileChooser.setInitialDirectory(new File(file != null ? file.getParent() : System.getenv("PWD")));
        
        if (file != null) {
            fileChooser.setInitialFileName(file.getAbsolutePath());
        }
        
        return fileChooser.showSaveDialog(stage);
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
    
}
