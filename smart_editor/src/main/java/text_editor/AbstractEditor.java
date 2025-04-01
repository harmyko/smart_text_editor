package main.java.text_editor;

public abstract class AbstractEditor implements Editable {
    protected StringBuilder text;

    public AbstractEditor() {
        text = new StringBuilder();
    }

    public AbstractEditor(String initialText) {
        this();
        text.append(initialText);
    }

    @Override
    public void addText(char character) {
        text.append(character);
    }

    @Override
    public void addText(String text) {
        this.text.append(text);
    }

    @Override
    public final void removeLastCharacter() {
        if (!text.isEmpty()) {
            text.deleteCharAt(text.length() - 1);
        }
    }

    @Override
    public void removeWord() {
        if (!text.isEmpty()) {
            int lastSpaceIndex = text.lastIndexOf(" ");
            if (lastSpaceIndex == -1) {
                text.setLength(0);
            } else {
                text.delete(lastSpaceIndex, text.length());
            }
        }
    }

    @Override
    public void removeWord(String word) {
        String textStr = text.toString();
        int index = textStr.lastIndexOf(word);

        while (index != -1) {
            boolean isWordBoundaryBefore = (index == 0 || Character.isWhitespace(textStr.charAt(index - 1)));
            boolean isWordBoundaryAfter = (index + word.length() == textStr.length() || Character.isWhitespace(textStr.charAt(index + word.length())));

            if (isWordBoundaryBefore && isWordBoundaryAfter) {
                text.delete(index, index + word.length());

                if (index > 0 && index < text.length() && text.charAt(index) == ' ') {
                    text.deleteCharAt(index);
                }
            }

            textStr = text.toString();
            index = textStr.lastIndexOf(word);
        }
    }

    @Override
    public int getTextLength() {
        return text.length();
    }

    @Override
    public String toString() {
        return text.toString();
    }
}
