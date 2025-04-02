package main.java.text_editor;

public interface Translatable extends Editable {
    String translateWord(String sourceWord);
    void translateString();
}
