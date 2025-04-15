package main.java.text_editor;

import java.util.*;

public class SpellCheckEditor
		extends Editor
		implements Transformable {

	public SpellCheckEditor(ArrayList<String> words) {
		super();
		setDictionary(words);
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

		for (int i = 0; i < words.length; i++) {
			if (checkWord(words[i])) {
				checkedString.append(words[i]);
			}

			if (i < words.length - 1) {
				checkedString.append(" ");
			}
		}

		text = checkedString;
	}

	public void removeWord(String word) {
		super.removeWord(word);
		removeWordFromDictionary(word);
	}

	@Override
	public Editor cloneEditor() {
		SpellCheckEditor clone = new SpellCheckEditor(new ArrayList<>(this.dictionary));
		clone.addText(this.text.toString());
		return clone;
	}
}
