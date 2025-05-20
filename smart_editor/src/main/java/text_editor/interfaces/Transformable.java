package main.java.text_editor.interfaces;

/**
 * The Transformable interface extends the Editable interface with transformation capabilities.
 * Classes implementing this interface can transform their text content.
 *
 * @author Ugnius Teišerskis
 */
public interface Transformable
        extends Editable {

    /**
     * Transforms the text content according to the specific implementation.
     */
    void transform();
}
