package test;

import text_editor.*;

public class EditorTest {
	
	   	public static void main(String[] args) {
	        // Testing Editor
	        Editor editor = new Editor();
	        System.out.println("Testing Editor:");
	        editor.append("Hello world");
	        System.out.println(editor);
	        editor.removeWord();
	        System.out.println("After removing last word " + editor);
	        editor.append(" love life positivity lamp love freedom love peace");
	        System.out.println("After appending " + editor);
	        editor.removeWord("love");
	        System.out.println("After removing all instances of \"love\" " + editor);
	        System.out.println();

	        // Testing SpellCheckEditor
	        SpellCheckEditor spellCheckEditor = new SpellCheckEditor("src/sourceDictionary.txt");
	        System.out.println("Testing SpellCheckEditor:");
	        System.out.println("Is 'hello' a valid word? " + spellCheckEditor.checkWord("hello"));
	        spellCheckEditor.addWord("newword");
	        System.out.println("Is 'newword' a valid word? " + spellCheckEditor.checkWord("newword"));
	        spellCheckEditor.removeWord("newword");
	        System.out.println("After removing 'newword': " + spellCheckEditor.checkWord("newword"));
	        System.out.println();

	        // Testing TranslateEditor
	        TranslateEditor translateEditor = new TranslateEditor("src/sourceDictionary.txt", "src/targetDictionary.txt");
	        System.out.println("Testing TranslateEditor:");
	        System.out.println("Translation of 'hello': " + translateEditor.translateWord("hello"));
	        System.out.println("Translation of 'world': " + translateEditor.translateWord("world"));
	        translateEditor.append("hello world love peace freedom lamp");
	        System.out.println("Before translation:\n" + translateEditor);
	        translateEditor.translateString();
	        System.out.println("After translation:\n" + translateEditor);
	        
	        System.out.println();
	    }
}
