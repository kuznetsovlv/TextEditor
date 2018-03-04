
package texteditor.history;

import javafx.scene.control.IndexRange;


class Item {
    String text;
    IndexRange selection;

    public Item(String text, IndexRange selection) {
        this.text = text;
        this.selection = selection;
    }

    public String getText() {
        return text;
    }

    public IndexRange getSelection() {
        return selection;
    }
}
