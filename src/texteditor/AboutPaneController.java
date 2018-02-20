package texteditor;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;

public class AboutPaneController extends Application implements Initializable {
    
    @FXML
    private Hyperlink hyperlink;
    
    @FXML
    private Button closeButton;
    
    @FXML
    public void closeButtonClick(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        hyperlink.setOnAction((ActionEvent event) -> {
            getHostServices().showDocument(hyperlink.getText());
        });
    }    

    @Override
    public void start(Stage primaryStage) throws Exception {}
    
}
