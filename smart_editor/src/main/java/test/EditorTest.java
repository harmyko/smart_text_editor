package main.java.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import main.java.text_editor.InvalidWordException;
import main.java.text_editor.SpellCheckEditor;
import main.java.text_editor.TranslateEditor;

public class EditorTest {

	   	public static void main(String[] args) {

			   	ArrayList<String> sourceWords = new ArrayList<>();
				try (BufferedReader reader = new BufferedReader(new FileReader("smart_editor\\src\\main\\resources\\sourceDictionary.txt"))) {
					String line;
					while ((line = reader.readLine()) != null) {
						for (String word : line.split("\\s+")) {
							sourceWords.add(word.toLowerCase());
						}
					}
				} catch (IOException e) {
					throw new IllegalArgumentException(e);
				}

				ArrayList<String> targetWords = new ArrayList<>();
				try (BufferedReader reader = new BufferedReader(new FileReader("smart_editor\\src\\main\\resources\\targetDictionary.txt"))) {
					String line;
					while ((line = reader.readLine()) != null) {
						for (String word : line.split("\\s+")) {
							targetWords.add(word.toLowerCase());
						}
					}
				} catch (IOException e) {
					throw new IllegalArgumentException(e);
				}

				SpellCheckEditor spellCheckEditor = new SpellCheckEditor(sourceWords);
				TranslateEditor translateEditor = new TranslateEditor(sourceWords, targetWords);

				System.out.println("Testing SpellCheckEditor:");
				try {
					System.out.println("Is 'hello' a valid word? " + spellCheckEditor.checkWord("hello"));
					System.out.println("Is 'helo' valid? " + spellCheckEditor.checkWord("helo"));
				} catch (InvalidWordException e) {
					System.err.println(e.getMessage());
				}

				System.out.println("\nTesting TranslateEditor:");
				try {
					System.out.println("Translation of 'hello': " + translateEditor.translateWord("hello"));
					System.out.println("Translation of 'helo': " + translateEditor.translateWord("helo"));
				} catch (InvalidWordException e) {
					System.err.println(e.getMessage());
				}
	    }
}
