package main.java.text_editor;

import java.util.Set;

public interface SpellCheckable extends Editable {
    void setDictionary(Set<String> words);
    Set<String> getDictionary();
    void addWordToDictionary(String word);
    void removeWordFromDictionary(String word);
    boolean checkWord(String word);
    void checkString();
    void removeWord(String word);
}
