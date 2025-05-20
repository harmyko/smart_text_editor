package main.java.text_editor.factory;

import main.java.text_editor.editors.Editor;

/**
 * The abstract EditorFactory class defines a factory method for creating editors.
 * This class follows the Factory Method design pattern to encapsulate editor creation.
 *
 * @author Ugnius Teišerskis
 */
public abstract class EditorFactory {

    /**
     * Creates and returns a new Editor instance.
     * The concrete implementation determines the specific type of editor created.
     *
     * @return a new Editor instance
     */
    public abstract Editor createEditor();
}

