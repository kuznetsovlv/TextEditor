package texteditor.windows;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
        closeDialog(event);
        
        if (reaction != null) {
            reaction.yesReaction(event);
        }
    }
    
    @FXML
    public void noButtonClick(ActionEvent event) {
        closeDialog(event);
        
        if (reaction != null) {
            reaction.noReaction(event);
        }
    }
    
    @FXML
    public void cancelButtonClick(ActionEvent event) {
        closeDialog(event);
        
        if (reaction != null) {
            reaction.cancelReaction(event);
        }
    }
    
    private void closeDialog(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getTarget()).getScene().getWindow();
        if (stage.isShowing()) {
             stage.close();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {}    
    
}
