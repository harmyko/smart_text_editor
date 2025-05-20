package main.java.text_editor.editors;

import main.java.text_editor.interfaces.Transformable;

import java.io.Serializable;
import java.util.*;

/**
 * The TranslateEditor extends the base Editor class to provide text translation functionality.
 * It maintains a mapping between source and target languages and can transform text
 * by translating words according to this mapping.
 *
 * <p>This editor preserves punctuation and word boundaries during translation.</p>
 *
 * @author Ugnius Teišerskis
 */
public class TranslateEditor
		extends Editor
		implements Transformable, Cloneable, Serializable {

	/** Map maintaining the translation between source words and their target language equivalents */
	private LinkedHashMap<String, String> translationMap;

	/**
	 * Constructs a TranslateEditor with source and target language dictionaries.
	 * The dictionaries must have the same size, where each word at index i in sourceWords
	 * corresponds to the translation at index i in targetWords.
	 *
	 * @param sourceWords list of words in the source language
	 * @param targetWords list of corresponding translations in the target language
	 * @throws IllegalArgumentException if the source and target dictionaries have different sizes
	 */
	public TranslateEditor(ArrayList<String> sourceWords, ArrayList<String> targetWords) {
		super();
		createTranslationMap(sourceWords, targetWords);
	}

	/**
	 * Default constructor that initializes an empty TranslateEditor.
	 */
	public TranslateEditor() {
		super();
	}

	/**
	 * Constructs a TranslateEditor with a pre-defined translation map.
	 *
	 * @param translationMap the map containing source words as keys and their translations as values
	 */
	public TranslateEditor(LinkedHashMap<String, String> translationMap) {
		super();
		this.translationMap = translationMap;
	}

	/**
	 * Creates a translation map from source and target language word lists.
	 * Each word in sourceWords is mapped to the corresponding word in targetWords.
	 *
	 * @param sourceWords list of words in the source language
	 * @param targetWords list of corresponding translations in the target language
	 * @throws IllegalArgumentException if the source and target dictionaries have different sizes
	 */
	public void createTranslationMap(ArrayList<String> sourceWords, ArrayList<String> targetWords) {
		if (sourceWords.size() != targetWords.size()) {
		    throw new IllegalArgumentException("Source and target dictionaries must have the same size.");
		}
		
		translationMap = new LinkedHashMap<>();
		
		for (int i = 0; i < sourceWords.size(); i++) {
			translationMap.put(sourceWords.get(i), targetWords.get(i));
		}
	}

	/**
	 * Translates a single word from the source language to the target language.
	 * If the word is not found in the translation map, it is returned unchanged.
	 *
	 * @param sourceWord the word to translate
	 * @return the translated word, or the source word if no translation exists
	 */
	public String translateWord(String sourceWord) {
	if (!translationMap.containsKey(sourceWord)) {
			return sourceWord;
		}
		return translationMap.get(sourceWord);
	}

	/**
	 * Transforms the text by translating all recognizable words.
	 * Words not found in the translation map are preserved as-is.
	 * Punctuation and word boundaries are preserved during translation.
	 */
	public void transform() {
		StringBuilder translatedString = new StringBuilder();
		String[] words = text.toString().split("\\s+");

		for (int i = 0; i < words.length; i++) {
			String word = words[i];

			String leadingPunct = "";
			String trailingPunct = "";
			String cleanWord = word;

			while (!cleanWord.isEmpty() && !Character.isLetterOrDigit(cleanWord.charAt(0))) {
				leadingPunct += cleanWord.charAt(0);
				cleanWord = cleanWord.substring(1);
			}

			while (!cleanWord.isEmpty() && !Character.isLetterOrDigit(cleanWord.charAt(cleanWord.length() - 1))) {
				trailingPunct = cleanWord.charAt(cleanWord.length() - 1) + trailingPunct;
				cleanWord = cleanWord.substring(0, cleanWord.length() - 1);
			}

			String translatedWord = cleanWord.isEmpty() ? "" : translateWord(cleanWord.toLowerCase());

			translatedString.append(leadingPunct).append(translatedWord).append(trailingPunct);

			if (i < words.length - 1) {
				translatedString.append(" ");
			}
		}

		text = translatedString;
	}

	/**
	 * Removes a word and its translation from the translation map.
	 *
	 * @param word the source word to remove from the translation map
	 */
	public void removeTranslation(String word) {
		translationMap.remove(word);
	}

	/**
	 * Creates and returns a clone of this TranslateEditor.
	 *
	 * @return a clone of this TranslateEditor
	 * @throws CloneNotSupportedException if cloning is not supported
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		TranslateEditor clone = (TranslateEditor) super.clone();
		clone.translationMap = new LinkedHashMap<>(this.translationMap);
		return clone;
	}
}
