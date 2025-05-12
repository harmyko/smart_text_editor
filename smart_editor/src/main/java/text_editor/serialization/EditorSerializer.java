package main.java.text_editor.serialization;

import main.java.text_editor.editors.Editor;

import java.io.*;

public class EditorSerializer {

    public static void saveEditor(Editor editor, String fileName) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(fileName)))) {
            oos.writeObject(editor);
        }
    }

    public static Editor loadEditor(String fileName) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(fileName)))) {
            return (Editor) ois.readObject();
        }
    }
}