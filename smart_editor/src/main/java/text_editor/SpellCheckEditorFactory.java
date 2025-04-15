package main.java.text_editor;

import java.util.ArrayList;

public class SpellCheckEditorFactory extends EditorFactory {
    @Override
    public Editor createEditor(Object... args) {
        if (args.length != 1 || !(args[0] instanceof ArrayList)) {
            throw new IllegalArgumentException("SpellCheckEditor requires a dictionary.");
        }

        ArrayList<String> dictionary = (ArrayList<String>) args[0];
        return new SpellCheckEditor(dictionary); // Pass the dictionary to the editor
    }
}
