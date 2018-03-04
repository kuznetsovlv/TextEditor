package texteditor.windows;

import javafx.event.ActionEvent;

public interface DialogReaction {
    
    public void yesReaction(ActionEvent event);
    public void noReaction(ActionEvent event);
    public default void cancelReaction(ActionEvent event) {}
}
