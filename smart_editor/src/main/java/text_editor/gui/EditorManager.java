package main.java.text_editor.gui;

import main.java.text_editor.editors.*;
import main.java.text_editor.factory.*;

import java.io.*;
import java.util.ArrayList;

public class EditorManager {
    private Editor currentEditor;
    private EditorFactory spellCheckFactory;
    private EditorFactory translateFactory;

    private static final String DEFAULT_SOURCE_DICTIONARY_PATH = "smart_editor/src/main/resources/source_dictionary.txt";
    private static final String DEFAULT_TARGET_DICTIONARY_PATH = "smart_editor/src/main/resources/target_dictionary.txt";
    private static final String RESOURCE_PATH = "smart_editor/src/main/resources/";

    public EditorManager() {
        initializeFactories();

        switchToSpellCheckEditor();
    }

    private void initializeFactories() {
        spellCheckFactory = new SpellCheckEditorFactory();
        translateFactory = new TranslateEditorFactory();
    }

    public void switchToSpellCheckEditor() {
        currentEditor = spellCheckFactory.createEditor();

        if (currentEditor instanceof SpellCheckEditor) {
            ArrayList<String> words = readWordsFromFile(DEFAULT_SOURCE_DICTIONARY_PATH);
            ((SpellCheckEditor) currentEditor).setDictionary(words);
        }
    }

    public void switchToTranslateEditor() {
        currentEditor = translateFactory.createEditor();

        if (currentEditor instanceof TranslateEditor) {
            ArrayList<String> sourceWords = readWordsFromFile(DEFAULT_SOURCE_DICTIONARY_PATH);
            ArrayList<String> targetWords = readWordsFromFile(DEFAULT_TARGET_DICTIONARY_PATH);
            ((TranslateEditor) currentEditor).createTranslationMap(sourceWords, targetWords);
        }
    }

    public Editor getCurrentEditor() {
        return currentEditor;
    }

    public void setCurrentEditor(Editor editor) {
        this.currentEditor = editor;
    }

    public String getResourcePath() {
        return RESOURCE_PATH;
    }

    public static ArrayList<String> readWordsFromFile(String filePath) {
        ArrayList<String> words = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                for (String word : line.split("\\s+")) {
                    if (!word.isEmpty()) {
                        words.add(word.toLowerCase());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Warning: " + e.getMessage());

            if (filePath.contains("source_dictionary")) {
                words.add("hello");
                words.add("world");
            } else if (filePath.contains("target_dictionary")) {
                words.add("labas");
                words.add("pasaulis");
            }
        }
        return words;
    }
}