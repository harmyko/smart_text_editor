package main.java.text_editor.exceptions;

/**
 * The InvalidWordException is thrown when an operation involves an invalid or
 * unrecognized word. This exception retains the problematic word for inspection.
 *
 * @author Ugnius Teišerskis
 */
public class InvalidWordException
        extends EditorException {

    /** The invalid word that caused the exception */
    private final String word;

    /**
     * Constructs a new InvalidWordException with the specified word and error message.
     *
     * @param word the invalid word that caused the exception
     * @param message the detailed error message
     */
    public InvalidWordException(String word, String message) {
        super(message);
        this.word = word;
    }

    /**
     * Gets the invalid word that caused this exception.
     *
     * @return the invalid word
     */
    public String getWord() {
        return word;
    }
}
