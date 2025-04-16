package main.java.text_editor;

public class TranslateEditorFactory extends EditorFactory {

    @Override
    public Editor createEditor() {
        return new TranslateEditor(dictionary, targetDictionary);
    }
}
