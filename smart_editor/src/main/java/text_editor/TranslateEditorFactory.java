package main.java.text_editor;

import java.util.ArrayList;

public class TranslateEditorFactory extends EditorFactory {
    @Override
    public Editor createEditor(Object... args) {
        if (args.length != 2 || !(args[0] instanceof ArrayList) || !(args[1] instanceof ArrayList)) {
            throw new IllegalArgumentException("TranslateEditor requires both source and target word lists.");
        }

        ArrayList<String> sourceWords = (ArrayList<String>) args[0];
        ArrayList<String> targetWords = (ArrayList<String>) args[1];
        return new TranslateEditor(sourceWords, targetWords);
    }
}
