package texteditor;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class DialogPaneController implements Initializable {
    
    @FXML
    private Text text;
    
    @FXML
    Button cancelButton;
    
    private DialogReaction reaction;
    
    public void setDialog(DialogReaction reaction, String text) {
        this.text.setText(text);
        this.reaction = reaction;
    }
    
    @FXML
    public void yesButtonClick(ActionEvent event) {
        reaction.yesReaction(event);
    }
    
    @FXML
    public void noButtonClick(ActionEvent event) {
        reaction.noReaction(event);
    }
    
    @FXML
    public void cancelButtonClick(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {}    
    
}
