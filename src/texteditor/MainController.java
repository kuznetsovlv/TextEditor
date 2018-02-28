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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.IndexRange;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
    private Object occupiedBy;
    
    private boolean dataUnsaved;
    
    private File file;
    
    /*EVENT HANDLERS*/
    @FXML
    public void newFileCreate(ActionEvent event) {
        creAteFile();
    }
    
    @FXML
    public void openFile(ActionEvent event) {
        openOtherFile();
    }
    
    @FXML
    public void saveCurrentFile(ActionEvent event) {
        saveFile(null);
    }
    
    @FXML
    public void saveNewFile(ActionEvent event) {
        openSaveDialog(null);
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
    
    /*INITIALIZER*/
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
                } else if(key.equals(KeyCode.S)) {
                    event.consume();
                    if(event.isShiftDown()) {
                        openSaveDialog(null);
                    } else {
                        saveFile(null);
                    }
                } else if(key.equals(KeyCode.N)) {
                    event.consume();
                    creAteFile();
                } else if(key.equals(KeyCode.O)) {
                    event.consume();
                    openOtherFile();
                }
            }
        });
        
        history = new History();
        historyManager = new HistoryManager(this);
        historyManager.start();
        openCurrentFile();        
        
        occupiedBy = null;
    }
    
    /*PUBLIC CONTROL METHODS*/
    
    /*history control*/
    public void addHistory() {
        if (!textArea.getText().equals(history.getCurrent())) {
            history.add(textArea.getText(), textArea.getSelection());
            enableHistoryItems();
        }
    }
    
    public void redoText() {
        history.redo();
        setText();
    }
    
    public void resetHistory(String initial) {
        history.reset(initial);
        setText();
        dataUnsaved = false;
    }
    
    public void undoText() {
        history.undo();
        setText();
    }
    
    /*access control methods*/
    public boolean isAvailableFor(Object obj) {
        return occupiedBy == null || occupiedBy == obj;
    }
    
    public void setOccupied(Object obj) {
        occupiedBy = obj;
    }
    
    public void setFree() {
        occupiedBy = null;
    }
    
    /*getters and setters*/
    public String getText() {
        return textArea.getText();
    }
    
    public void setDataUnsaved(boolean dataUnsaved) {
        this.dataUnsaved = dataUnsaved;
    }
    
    private void enableHistoryItems() {
        undoMenuItem.setDisable(history.getCurrentIndex() <= 0);
        redoMenuItem.setDisable(history.getCurrentIndex() >= history.size() - 1);
    }
    
    private void setText() {
        IndexRange selection = history.getCurrentSelection();
        textArea.setText(history.getCurrent());
        textArea.selectRange(selection.getStart(), selection.getEnd());
        enableHistoryItems();
    }
    
    /*controls*/
    public void exit() {
        if (dataUnsaved) {
            askForSaveFile(new DialogReaction() {
                @Override
                public void yesReaction(ActionEvent event) {
                    openSaveDialog(() -> System.exit(0));
                }

                @Override
                public void noReaction(ActionEvent event) {
                    System.exit(0);
                }
            }, "You have unsaved data. Would you like to save it before exit?");
        } else {
            System.exit(0);
        }
    }
    
    /*PRIVATE METHODS*/
    private void askForSaveFile(DialogReaction reaction, String question) {
        openDialog(reaction, question, "Unsaved changes");
    }
    
    private void chooseFile() {
//        if (fileChooser == null) {
//            return;
//        }
//        
//        File selectedFile = fileChooser.process(file);
//        
//        if (selectedFile != null) {
//            file = selectedFile;
//            openCurrentFile();
//        }
    }
    
    private void creAteFile() {
        if (dataUnsaved) {
            askForSaveFile(new DialogReaction() {
                @Override
                public void yesReaction(ActionEvent event) {
                    if (openSaveDialog(null) != null) {
                        dataUnsaved = false;
                        startCreateProcess();
                    }
                }

                @Override
                public void noReaction(ActionEvent event) {
                    startCreateProcess();
                }
            }, "Yuo have unsaved data. Would you like to save it before creating new file?");
        } else {
            startCreateProcess();
        }
    }
    
    private void openCurrentFile() {
        if (file == null) {
            historyManager.resetHistory("");
        } else {
            new ThreadFileReader(file, this);
        }
        
        updateTitle();
    }
    
    private void openDialog(DialogReaction reaction, String question, String title) {
        try {
            new DialogCreator(reaction, question, title);
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void openOtherFile() {
        if (dataUnsaved) {
            askForSaveFile(new DialogReaction() {
                @Override
                public void yesReaction(ActionEvent event) {
                    if (openSaveDialog(null) != null) {
                        dataUnsaved = false;
                        chooseFile();
                    }
                }

                @Override
                public void noReaction(ActionEvent event) {
                    chooseFile();
                }
            }, "You have unsaved data. Would you like to save it before open new file?");
        } else {
            chooseFile();
        }
    }
    
    private File openSaveDialog(Callback callback) {
//        if (destinationCooser == null) {
//            return null;
//        }
//        
//        File selectedFile = destinationCooser.process(file);
//        
//        if (selectedFile != null) {
//            file = selectedFile;
//            saveFile(callback);
//        }
//        
//        return selectedFile;
        return null;
    }
    
    private void saveFile(Callback callback) {
        if (file != null) {
            startSaveProcess(callback);
            updateTitle();
        } else {
            openSaveDialog(callback);
        }
    }
    
    private void startCreateProcess() {
        resetHistory("");
        openSaveDialog(null);
    }
    
    private void startSaveProcess(Callback callback) {
        if (file != null) {
            ThreadFileWriter writer = new ThreadFileWriter(file, this, callback);
            writer.start();
            try {
                writer.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void updateTitle() {
//        if(titleSetter != null) {
//            titleSetter.process(file != null ? file.getAbsolutePath() : TextEditor.DEFAULT_TITLE);
//        }
    }
}
