package texteditor.windows;

import java.io.File;
import java.io.IOException;
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
import javafx.scene.layout.Pane;
import texteditor.history.HistoryManager;
import texteditor.fileoperations.SyncFileManager;
import texteditor.history.HistoryClient;

public class MainController implements Initializable, HistoryClient {
    
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
    
    private HistoryManager historyManager;
    private Runnable occupiedBy;
    
    private boolean dataUnsaved;
    
    private File file;
    
    /*EVENT HANDLERS*/
    @FXML
    public void newFileCreate(ActionEvent event) {
        createFile();
    }
    
    @FXML
    public void openFile(ActionEvent event) {
        openOtherFile();
    }
    
    @FXML
    public void saveCurrentFile(ActionEvent event) {
        saveFile();
    }
    
    @FXML
    public void saveNewFile(ActionEvent event) {
        if (selectOutput() != null) {
            saveFile();
        }
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
        DialogManager.instance().showAboutWindow();
    }
    
    @FXML
    public void textChange(Event event) {
        setDataUnsaved(true);
        
        int position = textArea.getAnchor() + textArea.getText().length() - historyManager.getCurrentText().length();
        if (position < textArea.getAnchor()) {
            position = textArea.getAnchor();
        }

        historyManager.add(textArea.getText(), position);
    }
    
    /*INITIALIZER*/
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setDataUnsaved(false);
        
        textArea.setOnKeyPressed((KeyEvent event) -> {
            if (event.isControlDown()) {
                KeyCode key = event.getCode();
                switch (key) {
                    case Z:
                        event.consume();
                        historyManager.undo();
                        break;
                    case Y:
                        event.consume();
                        historyManager.redo();
                        break;
                    case S:
                        event.consume();
                        if(event.isShiftDown()) {
                            selectOutput();
                        } else {
                            saveFile();
                        }   break;
                    case N:
                        event.consume();
                        createFile();
                        break;
                    case O:
                        event.consume();
                        openOtherFile();
                        break;
                    default:
                        break;
                }
            }
        });
        
        historyManager = new HistoryManager(this);
        historyManager.start();
        openCurrentFile();
        
        occupiedBy = null;
    }
    
    /*PUBLIC CONTROL METHODS*/
    /*access control methods*/
    @Override
    public boolean isAvailableFor(Runnable r) {
        return occupiedBy == null || occupiedBy == r;
    }
    
    @Override
    public void setOccupied(Runnable r) {
        occupiedBy = r;
    }

    @Override
    public void setFree() {
        occupiedBy = null;
    }
    
    /*getters and setters*/    
    @Override
    public void setState(String text, int anchor) {
        textArea.setText(text);
        
        textArea.selectRange(anchor, anchor);
        
        enableHistoryItems();
    }
    
    @Override
    public void notifyUpdatedHistory() {
        enableHistoryItems();
    }
    
    /*controls*/
    public void exit() {
        if (dataUnsaved) {
            DialogManager.instance().openUnsavedDataDialog(new DialogReaction() {
                @Override
                public void yesReaction(ActionEvent event) {
                    if (selectOutput() != null) {
                        System.exit(0);
                    }
                }

                @Override
                public void noReaction(ActionEvent event) {
                    System.exit(0);
                }
            });
        } else {
            System.exit(0);
        }
    }
    
    /*PRIVATE METHODS*/    
    private void chooseFile() {
        file = DialogManager.instance().openChoseDialog(file);
        openCurrentFile();
    }
    
    private void createFile() {
        if (dataUnsaved) {
            DialogManager.instance().openUnsavedDataDialog(new DialogReaction() {
                @Override
                public void yesReaction(ActionEvent event) {
                    if(selectOutput() != null && saveFile() != null) {
                        startCreateProcess();
                    }
                }

                @Override
                public void noReaction(ActionEvent event) {
                    startCreateProcess();
                }
            });
        } else {
            startCreateProcess();
        }
    }
    
    private void openCurrentFile() {
        setDataUnsaved(false);
        
        if (file == null) {
            historyManager.resetHistory("");
            updateTitle();
        } else {
            String text = null;
            
            try {
                text = SyncFileManager.instance().readTextFile(file, this);
                
                if(text == null) {
                    file = null;
                }
            } catch (InterruptedException ex) {
                file = null;
            } finally {
                final String resText = text != null ? text : "";
                updateTitle();
                historyManager.resetHistory(resText);
                textArea.setText(resText);
                setDataUnsaved(false);
            }
        }
    }
    
    private void openOtherFile() {
        if (dataUnsaved) {
            DialogManager.instance().openUnsavedDataDialog(new DialogReaction() {
                @Override
                public void yesReaction(ActionEvent event) {
                    if(selectOutput() != null) {
                        if(saveFile() != null) {
                            chooseFile();
                        }
                    }
                }

                @Override
                public void noReaction(ActionEvent event) {
                    chooseFile();
                }
            });
        } else {
            chooseFile();
        }
    }
    
    private File selectOutput() {
        file = DialogManager.instance().openSaveDialog(file);
        updateTitle();
        return file;
    }
    
    private File saveFile() {
        if (file == null) {
            file = selectOutput();
        }
        
        try {
            file = SyncFileManager.instance().writeTextFile(file, textArea.getText(), this);
        } catch(Exception e) {
            file = null;
        }
        
        setDataUnsaved(file == null);
        
        return file;
    }
    
    private void startCreateProcess() {
        historyManager.resetHistory("");
        file = null;
        updateTitle();
    }
    
    private void updateTitle() {
        DialogManager.instance().setMainTitle(file != null ? file.getAbsolutePath() : null);
    }
    
    private void setDataUnsaved(boolean dataUnsaved) {
        this.dataUnsaved = dataUnsaved;
    }
    
    private void enableHistoryItems() {
        undoMenuItem.setDisable(historyManager.getCurrentIndex() <= 0);
        redoMenuItem.setDisable(historyManager.getCurrentIndex() >= historyManager.size() - 1);
    }
}
