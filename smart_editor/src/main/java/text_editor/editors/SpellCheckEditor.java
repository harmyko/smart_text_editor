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

		boolean firstWordAdded = false;
		for (String word : words) {
			if (checkWord(word)) {
				if (firstWordAdded) {
					checkedString.append(" ");
				}
				checkedString.append(word);
				firstWordAdded = true;
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
