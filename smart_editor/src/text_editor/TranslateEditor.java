package text_editor;

import java.io.*;
import java.util.*;

public class TranslateEditor extends Editor {
	
	private Set<String> sourceDictionary;
	private Set<String> targetDictionary;
	private Map<String, String> translationMap;
	
	public TranslateEditor(String sourceFilename, String targetFilename) {
		super();
		sourceDictionary = openDictionaryFromFile(sourceFilename);
		targetDictionary = openDictionaryFromFile(targetFilename);
		createTranslationMap();
	}
	
    private Set<String> openDictionaryFromFile(String filename) {
        Set<String> words = new LinkedHashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                for (String word : line.split("\\s+")) {
                    words.add(word.toLowerCase());
                }
            }
        } catch (IOException e) {
        	throw new IllegalArgumentException("Error reading dictionary file: " + filename, e);
        }
        return words;
    }
    
	private void createTranslationMap() {
		Map<String, String> map = new HashMap<>();
		List<String> sourceWords = new ArrayList<>(sourceDictionary);
		List<String> targetWords = new ArrayList<>(targetDictionary);
		
		for (int i = 0; i < sourceWords.size(); i++) {
			map.put(sourceWords.get(i), targetWords.get(i));
		}
		
		translationMap = map;
	}
	
	public String translateWord(String sourceWord) {
	    String translatedWord = translationMap.get(sourceWord);
	    
	    if (translatedWord == null) {
	    	return " [No translation found for " + sourceWord + "] ";
	    }
	    
	    return translatedWord;
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
		if (translationMap.containsKey(word)) {
			translationMap.remove(translationMap.get(word));
			translationMap.remove(word);
		}
	}
	
    public String info() {
    	return "Translator [sourceDictionary=" + sourceDictionary + ", targetDictionary=" + targetDictionary + ".";
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
