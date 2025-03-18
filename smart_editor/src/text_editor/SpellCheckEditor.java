package text_editor;

import java.io.*;
import java.util.*;

public class SpellCheckEditor extends Editor {

		private Set<String> dictionary;
		
		public SpellCheckEditor(String filename) {
			super();
			setDictionaryFromFile(filename);
		}
		
		public void setDictionary(Set<String> words) {
			dictionary = words;
		}
		
	    public void setDictionaryFromFile(String filename) {
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
	        setDictionary(words);
	    }
		
		public Set<String> getDictionary() {
			return dictionary;
		}
		
	    public void addWord(String word) {
	        dictionary.add(word);
	    }
	    
	    public void removeWord(String word) {
	    	if (checkWord(word)) {
	    		dictionary.remove(word);
	    	}
	    }

	    public boolean checkWord(String word) {
	        return dictionary.contains(word.toLowerCase());
	    }
	    
	    public String info() {
	    	return "SpellCheckEditor [dictionary=" + dictionary + "]";
	    }
	    
	    @Override
	    public String toString() {
	    	return "SpellCheckEditor: " + text.toString();
	    }
		
}
