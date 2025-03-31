package main.java.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import main.java.text_editor.Editor;
import main.java.text_editor.SpellCheckEditor;
import main.java.text_editor.TranslateEditor;

public class EditorTest {

	   	public static void main(String[] args) {
	        // Testing Editor
	        Editor editor = new Editor();
	        System.out.println("Testing Editor:\t");
	        editor.addText("Hello world");
	        System.out.println(editor);
	        editor.removeWord();
	        System.out.println("After removing last word: " + editor);
	        editor.addText(" love life positivity lamp love freedom love peace");
	        System.out.println("After appending: " + editor);
	        editor.removeWord("love");
	        System.out.println("After removing all instances of \"love\":\t " + editor);
	        System.out.println();

	        // Testing SpellCheckEditor
	        Set<String> words = new LinkedHashSet<>();
			try (BufferedReader reader = new BufferedReader(new FileReader("smart_editor\\src\\main\\resources\\sourceDictionary.txt"))) {
				String line;
				while ((line = reader.readLine()) != null) {
					for (String word : line.split("\\s+")) {
						words.add(word.toLowerCase());
					}
				}
			} catch (IOException e) {
				throw new IllegalArgumentException(e);
			}

	        SpellCheckEditor spellCheckEditor = new SpellCheckEditor(words);
	        System.out.println("Testing SpellCheckEditor:");
	        System.out.println("Is 'hello' a valid word? " + spellCheckEditor.checkWord("hello"));
	        spellCheckEditor.addWordToDictionary("newword");
	        System.out.println("Is 'newword' a valid word? " + spellCheckEditor.checkWord("newword"));
	        spellCheckEditor.removeWordFromDictionary("newword");
	        System.out.println("After removing 'newword': " + spellCheckEditor.checkWord("newword"));
	        System.out.println();

	        // Testing TranslateEditor
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

	        TranslateEditor translateEditor = new TranslateEditor(sourceWords, targetWords);
	        System.out.println("Testing TranslateEditor:");
	        System.out.println("Translation of 'hello': " + translateEditor.translateWord("hello"));
	        System.out.println("Translation of 'world': " + translateEditor.translateWord("world"));
	        translateEditor.addText("hello world love peace freedom lamp");
	        System.out.println("Before translation:\n" + translateEditor);
	        translateEditor.translateString();
	        System.out.println("After translation:\n" + translateEditor);
	        System.out.println();

	        System.out.println();
	    }
}
