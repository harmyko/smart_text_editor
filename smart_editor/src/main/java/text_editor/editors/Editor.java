package main.java.text_editor.editors;

import main.java.text_editor.interfaces.Editable;

import java.io.Serializable;

public abstract class Editor
        implements Editable, Cloneable, Serializable {

    protected StringBuilder text;

    public Editor() {
        text = new StringBuilder();
    }

    public Editor(String initialText) {
        this();
        text.append(initialText);
    }

    public void addText(char character) {
        text.append(character);
    }

    public void addText(String text) {
        this.text.append(text);
    }

    public final void removeLastCharacter() {
        if (!text.isEmpty()) {
            text.deleteCharAt(text.length() - 1);
        }
    }

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

    public int getTextLength() {
        return text.length();
    }

    public abstract void transform();

    @Override
    public String toString() {
        return text.toString();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Editor clone = (Editor) super.clone();
        //clone.text = new StringBuilder(this.text.toString());
        return clone;
    }
}
