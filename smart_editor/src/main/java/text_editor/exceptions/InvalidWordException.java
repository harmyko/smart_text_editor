package main.java.text_editor.exceptions;

public class InvalidWordException extends EditorException {
    private final String word;

    public InvalidWordException(String word, String message) {
        super(message);
        this.word = word;
    }

    public String getWord() {
        return word;
    }
}
