
package texteditor.history;

class Item {
    private final String text;
    private final int anchor;

    public Item(String text, int anchor) {
        this.text = text;
        this.anchor = anchor;
    }

    public String getText() {
        return text;
    }

    public int getAnchor() {
        return anchor;
    }
}
