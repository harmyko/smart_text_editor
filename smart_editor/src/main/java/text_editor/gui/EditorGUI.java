package main.java.text_editor.gui;

import main.java.text_editor.editors.*;
import main.java.text_editor.factory.*;

import javax.swing.*;
import java.awt.*;

/**
 * Main GUI class for the text editor application.
 * This class coordinates the different components of the editor GUI.
 */
public class EditorGUI implements Runnable {
    private JFrame frame;
    private EditorPanel editorPanel;
    private ControlPanel controlPanel;
    private StatusPanel statusPanel;

    private EditorManager editorManager;

    public EditorGUI() {
        // Initialize the editor manager first
        this.editorManager = new EditorManager();

        // Create GUI components
        createGUI();
    }

    @Override
    public void run() {
        frame.setVisible(true);
    }

    private void createGUI() {
        // Create main frame
        frame = new JFrame("Text Editor Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 600);

        // Create panels with dependencies
        statusPanel = new StatusPanel();
        editorPanel = new EditorPanel(editorManager);
        controlPanel = new ControlPanel(editorManager, editorPanel, statusPanel);

        // Arrange panels in frame
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(controlPanel, BorderLayout.NORTH);
        mainPanel.add(editorPanel, BorderLayout.CENTER);
        mainPanel.add(statusPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
    }
}