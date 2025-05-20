package main.java.text_editor.exceptions;

/**
 * The EditorException class is a base exception class for all editor-related exceptions.
 * It provides a standardized format for error messages by prefixing them with "[Editor Error] ".
 *
 * @author Ugnius Teišerskis
 */
public class EditorException
        extends RuntimeException {

    /**
     * Constructs a new EditorException with the specified error message.
     * The message is automatically prefixed with "[Editor Error] ".
     *
     * @param message the error message
     */
    public EditorException(String message) {
        super("[Editor Error] " + message);
    }
}
