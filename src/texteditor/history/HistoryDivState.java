package texteditor.history;

class HistoryDivState {
    private final HistoryDivState prev;
    private final String increment;
    private final int position;
    private final int rest;

    HistoryDivState(String text, HistoryDivState prev) {
        String prevStr = prev != null ? prev.toString() : "";

        position = getBegin(text, prevStr);
        rest = getRest(text, prevStr);

        if (position + rest > Math.min(prevStr.length(), text.length())) {
            this.prev = null;
            increment = text;
        } else {
            this.prev = prev;
            increment = text.length() > 0
                    ?
                    rest > 0
                        ?
                    text.substring(position, text.length() - rest)
                        :
                    text.substring(position)
                    :
                    "";
        }
    }

    public int getCursorPosition() {
        return this.toString().length() - rest;
    }

    @Override
    public String toString() {
        if (prev == null) {
            return increment;
        }

        String prevStr = prev.toString();
        return prevStr.substring(0, position) + increment + (rest > 0 ? prevStr.substring(prevStr.length() - rest) : "");
    }
    
    private int getBegin(String text, String prevStr) {
        if (prevStr.length() == 0) {
            return 0;
        }

        int i;

        for(i = 0; i < Math.min(prevStr.length(), text.length()); ++i) {
            if(prevStr.charAt(i) != text.charAt(i)) {
                return i;
            }
        }

        return i;
    }

    private int getRest(String text, String prevStr) {
         if (prevStr.length() == 0) {
            return 0;
        }

        int i, j;

        for (i = 0, j = 1; j < Math.min(prevStr.length(), text.length()); ++i, ++j) {
            if(prevStr.charAt(prevStr.length() - j) != text.charAt(text.length() - j)) {
                return i;
            }
        }

        return i;
    }
}
