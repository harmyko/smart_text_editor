package main.java.text_editor.editors;

import main.java.text_editor.interfaces.Editable;

import java.io.Serializable;

/**
 * The abstract Editor class serves as the base class for all editor implementations.
 * It provides common text editing functionality like adding and removing text,
 * managing caret position, and basic text manipulation.
 *
 * <p>This class implements {@link Editable}, {@link Cloneable}, and {@link Serializable}
 * interfaces, providing a foundation for more specialized editors.</p>
 *
 * @author Ugnius Teišerskis
 */
public abstract class Editor
        implements Editable, Cloneable, Serializable {

    /** The text content of the editor stored as a StringBuilder for efficient manipulation */
    protected StringBuilder text;

    /** The current caret position within the text */
    protected int caretPosition;

    /**
     * Default constructor that initializes an empty editor with caret at position 0.
     */
    public Editor() {
        text = new StringBuilder();
        caretPosition = 0;
    }

    /**
     * Constructor that initializes the editor with the specified text
     * and places the caret at the end of the text.
     *
     * @param initialText the initial text to populate the editor with
     */
    public Editor(String initialText) {
        this();
        text.append(initialText);
        caretPosition = initialText.length();
    }

    /**
     * Adds a single character at the current caret position and advances the caret.
     *
     * @param character the character to add
     */
    public void addText(char character) {
        text.insert(caretPosition, character);
        caretPosition++;
    }

    /**
     * Adds a string at the current caret position and advances the caret
     * to the end of the inserted text.
     *
     * @param text the string to add
     */
    public void addText(String text) {
        this.text.insert(caretPosition, text);
        caretPosition += text.length();
    }

    /**
     * Removes the character immediately before the caret position and moves
     * the caret backward by one position.
     * If the text is empty, this method has no effect.
     */
    public final void removeLastCharacter() {
        if (!text.isEmpty()) {
            text.deleteCharAt(caretPosition - 1);
            caretPosition--;
        }
    }

    /**
     * Removes the word at the current caret position.
     * The method identifies a word by looking for characters that are not whitespace.
     * If no word is found or the text is empty, this method has no effect.
     */
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

    /**
     * Removes all occurrences of the specified word from the text.
     * Only complete words matching the specified word are removed.
     * A word is considered complete if it is surrounded by whitespace or text boundaries.
     *
     * @param word the word to remove from the text
     */
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

    /**
     * Sets the caret position in the text.
     * If the specified position is out of bounds, the position is adjusted to the closest valid position.
     *
     * @param position the new caret position
     */
    public void setCaretPosition(int position) {
        if (position < 0) {
            position = 0;
        } else if (position > text.length()) {
            position = text.length();
        }
        this.caretPosition = position;
    }

    /**
     * Gets the current caret position in the text.
     *
     * @return the current caret position
     */
    public int getCaretPosition() {
        return caretPosition;
    }

    /**
     * Gets the length of the text in the editor.
     *
     * @return the length of the text
     */
    public int getTextLength() {
        return text.length();
    }

    /**
     * Abstract method that transforms the text content according to the specific
     * editor implementation.
     * Subclasses must implement this method to define their transformation behavior.
     */
    public abstract void transform();

    /**
     * Returns a string representation of the editor content.
     *
     * @return the text content as a string
     */
    @Override
    public String toString() {
        return text.toString();
    }

    /**
     * Creates and returns a clone of this editor.
     *
     * @return a clone of this editor
     * @throws CloneNotSupportedException if cloning is not supported
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        Editor clone = (Editor) super.clone();
        clone.text = new StringBuilder(this.text.toString());
        return clone;
    }
}
