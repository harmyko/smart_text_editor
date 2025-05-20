package main.java.text_editor.factory;

import main.java.text_editor.editors.Editor;
import main.java.text_editor.editors.SpellCheckEditor;

/**
 * The SpellCheckEditorFactory creates SpellCheckEditor instances.
 * This factory implements the factory method to produce spell check editors.
 *
 * @author Ugnius Teišerskis
 */
public class SpellCheckEditorFactory
        extends EditorFactory {

    /**
     * Creates and returns a new SpellCheckEditor instance.
     *
     * @return a new SpellCheckEditor instance
     */
    @Override
    public Editor createEditor() {
        return new SpellCheckEditor();
    }
}
