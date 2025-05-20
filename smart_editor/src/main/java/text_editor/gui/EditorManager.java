package main.java.text_editor.gui;

import main.java.text_editor.editors.*;
import main.java.text_editor.factory.*;

import java.io.*;
import java.util.ArrayList;

/**
 * The EditorManager class manages editor instances and handles editor-specific operations.
 * It provides methods for switching between different editor types, managing resources,
 * and reading dictionary files.
 *
 * @author Ugnius Teišerskis
 */
public class EditorManager {

    /** The currently active editor */
    private Editor currentEditor;

    /** Factory for creating SpellCheckEditor instances */
    private EditorFactory spellCheckFactory;

    /** Factory for creating TranslateEditor instances */
    private EditorFactory translateFactory;

    /** Default path to the source dictionary file */
    private static final String DEFAULT_SOURCE_DICTIONARY_PATH = "smart_editor/src/main/resources/source_dictionary.txt";

    /** Default path to the target dictionary file */
    private static final String DEFAULT_TARGET_DICTIONARY_PATH = "smart_editor/src/main/resources/target_dictionary.txt";

    /** Base path for resource files */
    private static final String RESOURCE_PATH = "smart_editor/src/main/resources/";

    /**
     * Constructs a new EditorManager.
     * Initializes the factories and sets up a default SpellCheckEditor.
     */
    public EditorManager() {
        initializeFactories();
        switchToSpellCheckEditor();
    }

    /**
     * Initializes the editor factories.
     */
    private void initializeFactories() {
        spellCheckFactory = new SpellCheckEditorFactory();
        translateFactory = new TranslateEditorFactory();
    }

    /**
     * Switches to a SpellCheckEditor, preserving the current text content.
     * Loads the default dictionary for the spell check editor.
     */
    public void switchToSpellCheckEditor() {
        String text = "";
        if (getCurrentEditor() != null) {
            text = getCurrentEditor().toString();
        }

        currentEditor = spellCheckFactory.createEditor();
        currentEditor.addText(text);

        if (currentEditor instanceof SpellCheckEditor) {
            ArrayList<String> words = readWordsFromFile(DEFAULT_SOURCE_DICTIONARY_PATH);
            ((SpellCheckEditor) currentEditor).setDictionary(words);
        }
    }

    /**
     * Switches to a TranslateEditor, preserving the current text content.
     * Loads the default source and target dictionaries for the translate editor.
     */
    public void switchToTranslateEditor() {
        String text = "";
        if (getCurrentEditor() != null) {
            text = getCurrentEditor().toString();
        }

        currentEditor = translateFactory.createEditor();
        currentEditor.addText(text);

        if (currentEditor instanceof TranslateEditor) {
            ArrayList<String> sourceWords = readWordsFromFile(DEFAULT_SOURCE_DICTIONARY_PATH);
            ArrayList<String> targetWords = readWordsFromFile(DEFAULT_TARGET_DICTIONARY_PATH);
            ((TranslateEditor) currentEditor).createTranslationMap(sourceWords, targetWords);
        }
    }

    /**
     * Gets the currently active editor.
     *
     * @return the current editor instance
     */
    public Editor getCurrentEditor() {
        return currentEditor;
    }

    /**
     * Sets the current editor to the specified editor instance.
     *
     * @param editor the editor to set as current
     */
    public void setCurrentEditor(Editor editor) {
        this.currentEditor = editor;
    }

    /**
     * Gets the base path for resource files.
     *
     * @return the resource path
     */
    public String getResourcePath() {
        return RESOURCE_PATH;
    }

    /**
     * Reads words from a file and returns them as a list.
     * If the file cannot be read, a default set of words is returned.
     *
     * @param filePath the path to the file containing words
     * @return a list of words read from the file
     */
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