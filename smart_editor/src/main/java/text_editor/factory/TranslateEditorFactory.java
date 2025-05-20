package main.java.text_editor.factory;

import main.java.text_editor.editors.Editor;
import main.java.text_editor.editors.TranslateEditor;

/**
 * The TranslateEditorFactory creates TranslateEditor instances.
 * This factory implements the factory method to produce translation editors.
 *
 * @author Ugnius Teišerskis
 */
public class TranslateEditorFactory
        extends EditorFactory {

    /**
     * Creates and returns a new TranslateEditor instance.
     *
     * @return a new TranslateEditor instance
     */
    @Override
    public Editor createEditor() {
        return new TranslateEditor();
    }
}
