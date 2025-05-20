package main.java.launcher;

import main.java.text_editor.gui.EditorGUI;
import javax.swing.*;

/**
 * The entry point for the text editor application.
 * <p>
 * This class launches the GUI for the text editor using the {@link EditorGUI} class.
 * @author Ugnius Teišerskis
 */
public class EditorApp {

    /**
     * The main method that starts the text editor application.
     *
     * @param args the command-line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new EditorGUI());
    }
}