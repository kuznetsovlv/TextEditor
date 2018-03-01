package texteditor;

import java.io.IOException;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import texteditor.windows.DialogManager;
import texteditor.windows.MainController;

/**
 *
 * @author leonid
 */
public class TextEditor extends Application {
    
    public static final String DEFAULT_TITLE = "Text Editor";
    
    @Override
    public void start(Stage stage) throws IOException {
        DialogManager.instance(stage).setDefaultTitle(DEFAULT_TITLE);
        DialogManager.instance().start();
        
//        MainController controller = (MainController) loader.getController();
//        controller.setFileChooser((File file) -> {
//            FileChooser fileChooser = new FileChooser();
//            fileChooser.setTitle("Select file");
//            fileChooser.setInitialDirectory(new File(file != null ? file.getParent() : System.getenv("PWD")));
//            
//            return fileChooser.showOpenDialog(stage);
//        });
//        
//        controller.setDestinationChooser((File file) -> {
//            
//            
//            
//            
//            if (file != null) {
//                fileChooser.setInitialFileName(file.getAbsolutePath());
//            }
//            
//            return fileChooser.showSaveDialog(stage);
//        });
//        
//        controller.setTitleSetter((String title) -> {
//            stage.setTitle(title);
//            return null;
//        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private static class EventHandlerImpl implements EventHandler<KeyEvent> {

        public EventHandlerImpl() {
        }

        @Override
        public void handle(KeyEvent event) {
        }
    }
    
}
