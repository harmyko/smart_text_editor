package main.java.launcher;

import main.java.text_editor.gui.EditorGUI;
import javax.swing.*;

public class EditorApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new EditorGUI());
    }
}