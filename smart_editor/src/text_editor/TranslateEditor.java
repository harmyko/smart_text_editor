package text_editor;

import java.util.*;

public class TranslateEditor extends Editor {
	
	private ArrayList<String> sourceDictionary;
	private ArrayList<String> targetDictionary;
	private Map<String, String> translationMap;
	
	public TranslateEditor(ArrayList<String> sourceWords, ArrayList<String> targetWords) {
		super();
		sourceDictionary = sourceWords;
		targetDictionary = targetWords;
		createTranslationMap();
	}
	
	public ArrayList<String> getSourceDictionary() {
		return sourceDictionary;
	}

	public void setSourceDictionary(ArrayList<String> sourceDictionary) {
		this.sourceDictionary = sourceDictionary;
		createTranslationMap();
	}

	public ArrayList<String> getTargetDictionary() {
		return targetDictionary;
	}

	public void setTargetDictionary(ArrayList<String> targetDictionary) {
		this.targetDictionary = targetDictionary;
		createTranslationMap();
	}
    
	private void createTranslationMap() {
		if (sourceDictionary.size() != targetDictionary.size()) {
		    throw new IllegalArgumentException("Source and target dictionaries must have the same size.");
		}
		
		translationMap = new LinkedHashMap<>();
		
		for (int i = 0; i < sourceDictionary.size(); i++) {
			translationMap.put(sourceDictionary.get(i), targetDictionary.get(i));
		}
	}
	
	public String translateWord(String sourceWord) {
	    return translationMap.getOrDefault(sourceWord, " [No translation found for " + sourceWord + "] ");
	}
	
	public void translateString() {
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
    public String getInfo() {
    	return "Translator [sourceDictionary=" + sourceDictionary + ", targetDictionary=" + targetDictionary + "]";
    }
	
	@Override
	public void removeWord(String word) {
		removeTranslation(word);
		super.removeWord(word);
	}
	
	@Override
	public String toString() {
		return "TranslateEditor: " + text.toString();
	}
}
