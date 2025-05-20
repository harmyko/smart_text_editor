package main.java.text_editor.interfaces;

/**
 * The Editable interface defines the basic text editing capabilities.
 * Classes implementing this interface provide functionality for adding and removing text.
 *
 * @author Ugnius Teišerskis
 */
public interface Editable {

    /**
     * Adds a single character to the text.
     *
     * @param character the character to add
     */
    void addText(char character);

    /**
     * Removes the last character from the text.
     */
    void removeLastCharacter();
}
