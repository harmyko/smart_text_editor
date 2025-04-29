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


        System.out.println("*** Factory method demonstration! ***");

        EditorFactory spellCheckFactory = new SpellCheckEditorFactory();
        spellCheckFactory.setDictionary(sourceWords);

        EditorFactory translateEditorFactory = new TranslateEditorFactory();
        translateEditorFactory.setDictionary(sourceWords);
        translateEditorFactory.setTargetDictionary(targetWords);

        EditorFactory factory = spellCheckFactory;

        for (int i = 0; i < 2; i++) {
            Editor editor = factory.createEditor();
            editor.addText("hello world asdfghjkl love");
            System.out.println("Before transformation: " + editor);
            editor.transform();
            System.out.println("After transformation: " + editor);
            System.out.println();

            factory = translateEditorFactory;
        }
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
