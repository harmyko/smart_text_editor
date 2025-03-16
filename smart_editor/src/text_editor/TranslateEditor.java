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
	
	public String translate(String sourceWord) {
	    String translatedWord = translationMap.get(sourceWord);
	    
	    if (translatedWord == null) {
	    	return "No translation found for" + sourceWord + ".";
	    }
	    
	    return translatedWord;
	}
	
	@Override
	public void removeWord(String word) {
		if (translationMap.containsKey(word)) {
			removeWord(translationMap.get(word));
			translationMap.remove(word);
			super.removeWord(word);
		}
	}
	
	@Override
    public String toString() {
    	return "Translator [sourceDictionary=" + sourceDictionary + ", targetDictionary=" + targetDictionary + ".";
    }
}
