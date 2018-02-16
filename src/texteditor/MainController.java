package texteditor;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;

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
    
    private History history;
    
    
    
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
        shiftHistory(1);
    }
    
    @FXML
    public void undo(ActionEvent event) {
        shiftHistory(-1);
    }
    
    @FXML
    public void showAboutProgram(ActionEvent event) {
        System.out.println("About");
    }
    
    @FXML
    public void textChange(Event event) {
        new HistorySaver(this).start();
    }
    
    public void addHistory() {
        if (!textArea.getText().equals(history.getCurrent())) {
            history.add(textArea.getText());
            enableHistoryItems();
        }
    }
    
     public void undoHistory() {
        history.undo();
        textArea.setText(history.getCurrent());
        enableHistoryItems();
    }
    
    public void redoHistory() {
        history.redo();
        textArea.setText(history.getCurrent());
        enableHistoryItems();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        history = new History();
        disableItems(saveAsFileMenuItem, saveFileMenuItem, closeFileMenuItem);
    }
    
    private void disableItems (MenuItem ...items) {
        for(MenuItem item: items) {
            item.setDisable(true);
        }
    }
    
    private void enableHistoryItems() {
        int position = history.getCurrentIndex();
        undoMenuItem.setDisable(position <= 0);
        redoMenuItem.setDisable(position >= history.size() - 1);
    }
    
    private void shiftHistory(int direction) {
        new HistoryMover(this, direction).start();
    }
}
