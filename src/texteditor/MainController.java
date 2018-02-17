package texteditor;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class MainController implements Initializable {
    
    @FXML
    private MenuItem newFileMenuItem;
    
    @FXML
    private MenuItem openFileMenuItem;
    
    @FXML
    private MenuItem saveFileMenuItem;
    
    @FXML
    private MenuItem saveAsFileMenuItem;
    
    @FXML
    private MenuItem closeFileMenuItem;
    
    @FXML
    private MenuItem exitMenuItem;
    
    @FXML
    private MenuItem redoMenuItem;
    
    @FXML
    private MenuItem undoMenuItem;
    
    @FXML
    private MenuItem aboutMenuItem;
    
    @FXML
    private TextArea textArea;
    
    private boolean free = false;
       
    @FXML
    public void newFileCreate(ActionEvent event) {
        System.out.println("Creating new file");
    }
    
    @FXML
    public void openFile(ActionEvent event) {
        System.out.println("Opening file");
    }
    
    @FXML
    public void saveCurrentFile(ActionEvent event) {
        System.out.println("Saving current file");
    }
    
    @FXML
    public void saveNewFile(ActionEvent event) {
        System.out.println("Saving new file");
    }
    
    @FXML
    public void closeOpendFile(ActionEvent event) {
        System.out.println("Closing file");
    }
    
    @FXML
    public void exitProgram(ActionEvent event) {
        System.exit(0);
    }
    
    @FXML
    public void redo(ActionEvent event) {
        updateHistory(1);
    }
    
    @FXML
    public void undo(ActionEvent event) {
        updateHistory(-1);
    }
    
    @FXML
    public void showAboutProgram(ActionEvent event) {
        System.out.println("About");
    }
    
    @FXML
    public void textChange(Event event) {
        updateHistory(0);
    }
    
    public void undoText() {
        if (textArea.undoableProperty().get()) {
            textArea.undo();
        }
    }
    
    public void redoText() {
        if (textArea.redoableProperty().get()) {
            textArea.redo();
        }
    }
    
    public void enableHistoryItems() {
        System.out.println("Enabling:" + textArea.getText() + " " + textArea.undoableProperty().get() + " " + textArea.redoableProperty().get());
        undoMenuItem.setDisable(!textArea.undoableProperty().get());
        redoMenuItem.setDisable(!textArea.redoableProperty().get());
    }
    
    
    public boolean isFree() {
        return free;
    }
    
    public void setOccupied() {
        free = false;
    }
    
    public void setFree() {
        free = true;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textArea.setOnKeyPressed((KeyEvent event) -> {
            if (event.isControlDown()) {
                KeyCode key = event.getCode();
                System.out.println(event.getCharacter());
                if(key.equals(KeyCode.Z)) {
                    event.consume();
                    updateHistory(-1);
                } else if(key.equals(KeyCode.Y)) {
                    event.consume();
                    updateHistory(1);
                }
            }
        });
        
        disableItems(saveAsFileMenuItem, saveFileMenuItem, closeFileMenuItem);
        free = true;
    }
    
    private void disableItems (MenuItem ...items) {
        for(MenuItem item: items) {
            item.setDisable(true);
        }
    }
    
    private void updateHistory(int direction) {
        new HistoryManager(this, direction).start();
    }
}
