package main.java.text_editor;

import java.util.ArrayList;

public interface Translatable extends Editable {
    void createTranslationMap(ArrayList<String> sourceWords, ArrayList<String> targetWords);
    String translateWord(String sourceWord);
    void translateString();
    void removeTranslation(String word);
}
