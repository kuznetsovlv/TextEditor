package texteditor;

import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;
import texteditor.windows.DialogManager;

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
    }

    public static void main(String[] args) {
        launch(args);
    }    
}
