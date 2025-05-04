package main.java.text_editor.factory;

import main.java.text_editor.editors.Editor;
import main.java.text_editor.editors.TranslateEditor;

public class TranslateEditorFactory extends EditorFactory {

    @Override
    public Editor createEditor() {
        return new TranslateEditor(dictionary, targetDictionary);
    }
}
