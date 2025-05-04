package main.java.text_editor.factory;

import main.java.text_editor.editors.Editor;
import main.java.text_editor.editors.SpellCheckEditor;

public class SpellCheckEditorFactory extends EditorFactory {

    @Override
    public Editor createEditor() {
        return new SpellCheckEditor(dictionary);
    }
}
