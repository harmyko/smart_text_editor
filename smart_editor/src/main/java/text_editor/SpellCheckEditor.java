package main.java.text_editor;

import java.util.*;

public class SpellCheckEditor
		extends Editor
		implements Transformable, Cloneable {

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

	public boolean checkWord(String word) throws InvalidWordException {
		if (!dictionary.contains(word.toLowerCase())) {
			// throw new InvalidWordException(word, "Word '" + word + "' not found in dictionary.");
		}
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


	public void removeWord(String word) {
		super.removeWord(word);
		removeWordFromDictionary(word);
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		SpellCheckEditor clone = (SpellCheckEditor) super.clone();
		clone.dictionary = new ArrayList<String>(this.dictionary);
		return clone;
	}

}
