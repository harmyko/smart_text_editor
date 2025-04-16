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

        EditorFactory factory = new SpellCheckEditorFactory();
        factory.setDictionary(sourceWords);
        Editor editor = factory.createEditor();


        System.out.println("*** SpellChecker deep cloning demonstration! ***");
        editor.addText("hello world");
        System.out.println("Original Editor:\t" + editor);

        Editor editorClone = null;
        try {
            editorClone = (Editor) editor.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        System.out.println("Cloned Editor:\t\t" + editorClone);

        System.out.println("** Adding \"new_word\" the original editor! **");
        editor.addText(" new_word");

        System.out.println("Original Editor:\t" + editor);
        System.out.println("Cloned Editor:\t\t" + editorClone);
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
