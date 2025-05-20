package main.java.text_editor.serialization;

import main.java.text_editor.editors.Editor;

import java.io.*;

/**
 * The EditorSerializer class provides static methods for saving and loading editor instances.
 * It uses Java serialization to persist and restore editor state.
 *
 * @author Ugnius Teišerskis
 */
public class EditorSerializer {

    /**
     * Saves an editor instance to a file.
     *
     * @param editor the editor to save
     * @param fileName the path to the file where the editor will be saved
     * @throws IOException if an I/O error occurs during serialization
     */
    public static void saveEditor(Editor editor, String fileName) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(fileName)))) {
            oos.writeObject(editor);
        }
    }

    /**
     * Loads an editor instance from a file.
     *
     * @param fileName the path to the file containing the serialized editor
     * @return the deserialized editor instance
     * @throws IOException if an I/O error occurs during deserialization
     * @throws ClassNotFoundException if the class of the serialized object cannot be found
     */
    public static Editor loadEditor(String fileName) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(fileName)))) {
            return (Editor) ois.readObject();
        }
    }
}