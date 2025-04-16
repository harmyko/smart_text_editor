package main.java.text_editor;

public class SpellCheckEditorFactory extends EditorFactory {

    @Override
    public Editor createEditor() {
        return new SpellCheckEditor(dictionary);
    }
}
