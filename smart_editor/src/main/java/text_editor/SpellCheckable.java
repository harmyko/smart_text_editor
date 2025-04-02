package main.java.text_editor;

public interface SpellCheckable extends Editable {
    boolean checkWord(String word);
    void checkString();
}
