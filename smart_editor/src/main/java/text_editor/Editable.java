package main.java.text_editor;

public interface Editable {
    void addText(char character);
    void addText(String string);
    void removeLastCharacter();
    void removeWord();
    void removeWord(String word);
    int getTextLength();
    String toString();
}
