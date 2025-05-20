package main.java.text_editor.editors;

import main.java.text_editor.interfaces.Transformable;
import main.java.text_editor.prediction.WordPredictor;

import java.io.Serializable;
import java.util.*;

/**
 * The SpellCheckEditor extends the base Editor class to provide spell checking capabilities.
 * It maintains a dictionary of known words and can transform the text to highlight
 * misspelled words by surrounding them with tilde (~) characters.
 *
 * <p>This class also provides word prediction functionality based on the current
 * word being typed and the available dictionary.</p>
 *
 * @author Ugnius Teišerskis
 */
public class SpellCheckEditor
		extends Editor
		implements Transformable, Cloneable, Serializable {

	/** List of words used for spell checking */
	private ArrayList<String> dictionary;

	/** Word predictor used for suggesting words as the user types */
	private WordPredictor predictor;

	/**
	 * Constructs a SpellCheckEditor with the specified dictionary.
	 *
	 * @param words list of words to use as the spell checking dictionary
	 */
	public SpellCheckEditor(ArrayList<String> words) {
		super();
		setDictionary(words);
	}

	/**
	 * Default constructor that initializes an empty SpellCheckEditor with an empty dictionary.
	 */
	public SpellCheckEditor() {
		super();
		dictionary = new ArrayList<String>();
	}

	/**
	 * Sets the dictionary for this spell checker and initializes the word predictor
	 * with the same dictionary.
	 *
	 * @param words the list of words to use as the dictionary
	 */
	public void setDictionary(ArrayList<String> words) {
		dictionary = words;
		predictor = new WordPredictor(dictionary);
	}

	/**
	 * Gets the current dictionary used for spell checking.
	 *
	 * @return the list of words in the dictionary
	 */
	public ArrayList<String> getDictionary() {
		return dictionary;
	}

	/**
	 * Adds a new word to the dictionary.
	 *
	 * @param word the word to add to the dictionary
	 */
	public void addWordToDictionary(String word) {
		dictionary.add(word);
	}

	/**
	 * Removes a word from the dictionary if it exists.
	 *
	 * @param word the word to remove from the dictionary
	 */
	public void removeWordFromDictionary(String word) {
		if (checkWord(word)) {
			dictionary.remove(word);
		}
	}

	/**
	 * Checks if a word exists in the dictionary.
	 * The check is case-insensitive.
	 *
	 * @param word the word to check
	 * @return true if the word exists in the dictionary, false otherwise
	 */
	public boolean checkWord(String word) {
		return dictionary.contains(word.toLowerCase());
	}

	/**
	 * Transforms the text by marking misspelled words.
	 * Words not found in the dictionary are surrounded by tilde (~) characters.
	 * Punctuation is preserved during the transformation.
	 */
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

	/**
	 * Gets a list of word predictions based on the current word being typed.
	 *
	 * @param maxSuggestions the maximum number of suggestions to return
	 * @return a list of predicted words that start with the current word prefix
	 */
	public List<String> getPredictions(int maxSuggestions) {
		String currentWord = getCurrentWord();
		if (predictor != null && !currentWord.isEmpty()) {
			return predictor.predictWords(currentWord, maxSuggestions);
		}
		return new ArrayList<>();
	}

	/**
	 * Gets the current word at the caret position.
	 * A word is defined as a sequence of letter or digit characters.
	 *
	 * @return the current word at the caret position, or an empty string if no word is found
	 */
	public String getCurrentWord() {
		if (text.length() == 0 || caretPosition == 0) {
			return "";
		}

		String textStr = text.toString();

		int wordStart = caretPosition - 1;
		while (wordStart >= 0 && Character.isLetterOrDigit(textStr.charAt(wordStart))) {
			wordStart--;
		}
		wordStart++;

		int wordEnd = caretPosition;
		while (wordEnd < textStr.length() && Character.isLetterOrDigit(textStr.charAt(wordEnd))) {
			wordEnd++;
		}

		if (wordStart < wordEnd && wordStart < caretPosition) {
			return textStr.substring(wordStart, Math.min(caretPosition, wordEnd));
		}

		return "";
	}

	/**
	 * Creates and returns a clone of this SpellCheckEditor.
	 *
	 * @return a clone of this SpellCheckEditor
	 * @throws CloneNotSupportedException if cloning is not supported
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		SpellCheckEditor clone = (SpellCheckEditor) super.clone();
		clone.dictionary = new ArrayList<String>(this.dictionary);
		return clone;
	}
}
