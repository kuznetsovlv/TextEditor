package texteditor;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.IndexRange;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class MainController implements Initializable {
    
    @FXML
    private Pane borderPane;
    
    @FXML
    private MenuItem newFileMenuItem;
    
    @FXML
    private MenuItem openFileMenuItem;
    
    @FXML
    private MenuItem saveFileMenuItem;
    
    @FXML
    private MenuItem saveAsFileMenuItem;
    
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
    private HistoryManager historyManager;
    
    private boolean free = false;
    private boolean dataUnsaved;
    private Stage stage;
    private File file;
    private WindowProcessor<File, File> fileChooser;
       
    @FXML
    public void newFileCreate(ActionEvent event) {
        System.out.println("Creating new file");
    }
    
    @FXML
    public void openFile(ActionEvent event) {
        chooseFile();
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
    public void exitProgram(ActionEvent event) {
        exit();
    }
    
    @FXML
    public void redo(ActionEvent event) {
        historyManager.redo();
    }
    
    @FXML
    public void undo(ActionEvent event) {
        historyManager.undo();
    }
    
    @FXML
    public void showAboutProgram(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("AboutPane.fxml"));
        
        Scene scene = new Scene(root);
        
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("About");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    
    @FXML
    public void textChange(Event event) {
        dataUnsaved = true;
        historyManager.add();
    }
    
    public void addHistory() {
        if (!textArea.getText().equals(history.getCurrent())) {
            history.add(textArea.getText(), textArea.getSelection());
            enableHistoryItems();
        }
    }
    
    public void undoText() {
        history.undo();
        setText();
    }
    
    public void redoText() {
        history.redo();
        setText();
    }
    
    public void resetHistory(String initial) {
        history.reset(initial);
        dataUnsaved = false;
        setText();
    }
    
    private void enableHistoryItems() {
        undoMenuItem.setDisable(history.getCurrentIndex() <= 0);
        redoMenuItem.setDisable(history.getCurrentIndex() >= history.size() - 1);
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
    
    public void setFileChooser(WindowProcessor<File, File> processor) {
        fileChooser = processor;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dataUnsaved = false;
        
        textArea.setOnKeyPressed((KeyEvent event) -> {
            if (event.isControlDown()) {
                KeyCode key = event.getCode();
                if(key.equals(KeyCode.Z)) {
                    event.consume();
                    historyManager.undo();
                } else if(key.equals(KeyCode.Y)) {
                    event.consume();
                    historyManager.redo();
                }
            }
        });
        
        history = new History();
        historyManager = new HistoryManager(this);
        historyManager.start();
        openCurrentFile();        
        
        free = true;
    }
    
    private void setText() {
        IndexRange selection = history.getCurrentSelection();
        textArea.setText(history.getCurrent());
        textArea.selectRange(selection.getStart(), selection.getEnd());
        enableHistoryItems();
    }
    
    public void exit() {
        if (dataUnsaved) {
            askForSaveFile(new DialogReaction() {
                @Override
                public void yesReaction(ActionEvent event) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public void noReaction(ActionEvent event) {
                    System.exit(0);
                }
            }, "You have unsaved data. Would you like to save it?", "Unsaved changes");
        } else {
            System.exit(0);
        }
    }
    
    private void askForSaveFile(DialogReaction reaction,  String question, String title) {
        try {
            new DialogCreator(reaction, question, title);
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void openCurrentFile() {
        
        if (file == null) {
            resetHistory("");
        } else {
            System.out.println(file.getAbsolutePath());
        }
        
    }
    
    private void chooseFile() {
        File selectedFile = fileChooser.process(file);
//        
//        
//        
//        File selectedFile = 
        
        if (selectedFile != null) {
            file = selectedFile;
            openCurrentFile();
        }
    }
}
