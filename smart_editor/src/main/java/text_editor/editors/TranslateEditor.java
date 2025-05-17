package main.java.text_editor.editors;

import main.java.text_editor.exceptions.InvalidWordException;
import main.java.text_editor.interfaces.Transformable;

import java.io.Serializable;
import java.util.*;

public class TranslateEditor
		extends Editor
		implements Transformable, Cloneable, Serializable {

	private LinkedHashMap<String, String> translationMap;
	
	public TranslateEditor(ArrayList<String> sourceWords, ArrayList<String> targetWords) {
		super();
		createTranslationMap(sourceWords, targetWords);
	}

	public TranslateEditor() {
		super();
	}

	public TranslateEditor(LinkedHashMap<String, String> translationMap) {
		super();
		this.translationMap = translationMap;
	}
    
	public void createTranslationMap(ArrayList<String> sourceWords, ArrayList<String> targetWords) {
		if (sourceWords.size() != targetWords.size()) {
		    throw new IllegalArgumentException("Source and target dictionaries must have the same size.");
		}
		
		translationMap = new LinkedHashMap<>();
		
		for (int i = 0; i < sourceWords.size(); i++) {
			translationMap.put(sourceWords.get(i), targetWords.get(i));
		}
	}

	public String translateWord(String sourceWord) {
	if (!translationMap.containsKey(sourceWord)) {
			return sourceWord;
		}
		return translationMap.get(sourceWord);
	}
	
	public void transform() {
	    StringBuilder translatedString = new StringBuilder();
	    String[] words = text.toString().split("\\s+");

	    for (int i = 0; i < words.length; i++) {
	        translatedString.append(translateWord(words[i]));
	        if (i < words.length - 1) {
	            translatedString.append(" ");
	        }
	    }

	    text = translatedString;
	}

	public void removeTranslation(String word) {
		translationMap.remove(word);
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		TranslateEditor clone = (TranslateEditor) super.clone();
		clone.translationMap = new LinkedHashMap<>(this.translationMap);
		return clone;
	}

}
