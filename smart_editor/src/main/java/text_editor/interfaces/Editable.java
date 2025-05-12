package main.java.text_editor.interfaces;

import java.io.Serializable;

public interface Editable extends Serializable {
    void addText(char character);
    void addText(String string);
    void removeLastCharacter();
    void removeWord();
    void removeWord(String word);
}
