/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package texteditor;

import java.io.File;
import java.io.IOException;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author leonid
 */
public class TextEditor extends Application {
    
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TextEditorPane.fxml"));
        
        
        
        Parent root = loader.load();
        Scene scene = new Scene(root);
        
        stage.setTitle("Text Editor");
        stage.setScene(scene);
        stage.setOnCloseRequest((WindowEvent event) -> {
            event.consume();
            MainController controller = (MainController) loader.getController();
            controller.exit();
        });
        stage.show();
        
        MainController controller = (MainController) loader.getController();
        controller.setFileChooser((File file) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select file");
            fileChooser.setInitialDirectory(new File(file != null ? file.getParent() : System.getenv("PWD")));
            
            return fileChooser.showOpenDialog(stage);
        });
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
