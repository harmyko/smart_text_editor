package main.java.text_editor;

import java.util.*;

public class SpellCheckEditor extends Editor implements SpellCheckable {

		private Set<String> dictionary;
		
		public SpellCheckEditor(Set<String> words) {
			super();
			setDictionary(words);
		}
		
		public void setDictionary(Set<String> words) {
			dictionary = words;
		}
		
		public Set<String> getDictionary() {
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
	    
		public void checkString() {
		    StringBuilder checkedString = new StringBuilder();
		    String[] words = text.toString().split("\\s+");

		    for (int i = 0; i < words.length; i++) {
		        if (checkWord(words[i])) {
		        	checkedString.append(words[i]);
		        } else {
		        	checkedString.append("[WORD \"" + words[i] + "\" NOT FOUND]");
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
}
