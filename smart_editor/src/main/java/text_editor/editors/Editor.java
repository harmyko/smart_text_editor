package main.java.text_editor.editors;

import main.java.text_editor.interfaces.Editable;

import java.io.Serializable;

public abstract class Editor
        implements Editable, Cloneable, Serializable {

    protected StringBuilder text;
    protected int caretPosition;

    public Editor() {
        text = new StringBuilder();
        caretPosition = 0;
    }

    public Editor(String initialText) {
        this();
        text.append(initialText);
        caretPosition = initialText.length();
    }

    public void addText(char character) {
        text.insert(caretPosition, character);
        caretPosition++;
    }

    public void addText(String text) {
        this.text.insert(caretPosition, text);
        caretPosition += text.length();
    }

    public final void removeLastCharacter() {
        if (!text.isEmpty()) {
            text.deleteCharAt(caretPosition - 1);
            caretPosition--;
        }
    }

    public void removeWord() {
        if (!text.isEmpty() && caretPosition > 0) {
            int searchStart = caretPosition - 1;

            int wordStart = searchStart;
            while (wordStart > 0 && !Character.isWhitespace(text.charAt(wordStart - 1))) {
                wordStart--;
            }

            if (wordStart < caretPosition) {
                text.delete(wordStart, caretPosition);
                caretPosition = wordStart;
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

                if (index < caretPosition) {
                    caretPosition = Math.max(0, caretPosition - (word.length() + (index < text.length() && text.charAt(index) == ' ' ? 1 : 0)));
                }
            }

            textStr = text.toString();
            index = textStr.lastIndexOf(word);
        }
    }

    public void setCaretPosition(int position) {
        if (position < 0) {
            position = 0;
        } else if (position > text.length()) {
            position = text.length();
        }
        this.caretPosition = position;
    }

    public int getCaretPosition() {
        return caretPosition;
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
