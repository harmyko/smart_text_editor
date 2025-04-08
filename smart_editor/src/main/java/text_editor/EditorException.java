package main.java.text_editor;

public class EditorException extends RuntimeException {
    public EditorException(String message) {
        super("[Editor Error] " + message);
    }
}
