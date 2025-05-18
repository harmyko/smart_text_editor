package main.java.text_editor.editors;

import main.java.text_editor.exceptions.InvalidWordException;
import main.java.text_editor.interfaces.Transformable;

import java.io.Serializable;
import java.util.*;

public class SpellCheckEditor
		extends Editor
		implements Transformable, Cloneable, Serializable {

	private ArrayList<String> dictionary;

	public SpellCheckEditor(ArrayList<String> words) {
		super();
		setDictionary(words);
	}

	public SpellCheckEditor() {
		super();
		dictionary = new ArrayList<String>();
	}

	public void setDictionary(ArrayList<String> words) {
		dictionary = words;
	}

	public ArrayList<String> getDictionary() {
		return dictionary;
	}

	public void addWordToDictionary(String word) {
		dictionary.add(word);
	}

	public void removeWordFromDictionary(String word) {
		if (checkWord(word)) {
			dictionary.remove(word);
		}
	}

	public boolean checkWord(String word) {
		return dictionary.contains(word.toLowerCase());
	}

	public void transform() {
		StringBuilder checkedString = new StringBuilder();
		String[] words = text.toString().split("\\s+");

		for (int i = 0; i < words.length; i++) {
			String word = words[i];

			// Extract punctuation from the beginning and end
			String leadingPunct = "";
			String trailingPunct = "";
			String cleanWord = word;

			// Extract leading punctuation
			while (!cleanWord.isEmpty() && !Character.isLetterOrDigit(cleanWord.charAt(0))) {
				leadingPunct += cleanWord.charAt(0);
				cleanWord = cleanWord.substring(1);
			}

			// Extract trailing punctuation
			while (!cleanWord.isEmpty() && !Character.isLetterOrDigit(cleanWord.charAt(cleanWord.length() - 1))) {
				trailingPunct = cleanWord.charAt(cleanWord.length() - 1) + trailingPunct;
				cleanWord = cleanWord.substring(0, cleanWord.length() - 1);
			}

			if (!cleanWord.isEmpty()) {
				if (checkWord(cleanWord)) {
					checkedString.append(leadingPunct).append(cleanWord).append(trailingPunct);
				} else {
					checkedString.append(leadingPunct).append("~").append(cleanWord).append("~").append(trailingPunct);
				}
			} else {
				checkedString.append(word);
			}

			if (i < words.length - 1) {
				checkedString.append(" ");
			}
		}

		text = checkedString;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		SpellCheckEditor clone = (SpellCheckEditor) super.clone();
		clone.dictionary = new ArrayList<String>(this.dictionary);
		return clone;
	}

}
