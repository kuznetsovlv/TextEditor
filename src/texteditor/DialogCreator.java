package texteditor;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DialogCreator {
    public DialogCreator(DialogReaction reaction, String text) throws IOException {
        this(reaction, text, "");
    }
    public DialogCreator(DialogReaction reaction, String text, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DialogPane.fxml"));
        Parent root = loader.load();
        
        DialogPaneController controller = (DialogPaneController) loader.getController();
        controller.setDialog(reaction, text);
        
        Scene scene = new Scene(root);
        
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
