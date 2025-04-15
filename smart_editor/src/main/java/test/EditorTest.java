package main.java.test;

import main.java.text_editor.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class EditorTest {
    public static void main(String[] args) {

        ArrayList<String> sourceWords = readWordsFromFile("smart_editor\\src\\main\\resources\\sourceDictionary.txt");
        ArrayList<String> targetWords = readWordsFromFile("smart_editor\\src\\main\\resources\\targetDictionary.txt");


        // *** Testing SpellCheckEditor Factory ***

        EditorFactory spellCheckFactory = new SpellCheckEditorFactory();
        Editor spellCheckEditor = spellCheckFactory.createEditor(sourceWords);

        spellCheckEditor.addText("hello asjfasg world");
        Editor spellCheckEditorClone = spellCheckEditor.cloneEditor();
        spellCheckEditor.transform();

        System.out.println("Before Transforming (Cloned Editor): " + spellCheckEditorClone);
        System.out.println("After Transforming (Original Editor): " + spellCheckEditor);
        System.out.println();


        // *** Testing TranslateEditor Factory ***

        EditorFactory translateEditorFactory = new TranslateEditorFactory();
        Editor translateEditor = translateEditorFactory.createEditor(sourceWords, targetWords);
        translateEditor.addText("hello world");

        Editor translateEditorClone = translateEditor.cloneEditor();
        translateEditor.transform();

        System.out.println("Before Transforming (Cloned Editor): " + translateEditorClone);
        System.out.println("After Transforming (Original Editor): " + translateEditor);
    }

    private static ArrayList<String> readWordsFromFile(String filePath) {
        ArrayList<String> words = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                for (String word : line.split("\\s+")) {
                    words.add(word.toLowerCase());
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return words;
    }
}
