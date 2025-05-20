package main.java.text_editor.gui;

import javax.swing.*;
import java.awt.*;

/**
 * The EditorGUI class represents the main graphical user interface for the text editor application.
 * It creates and arranges the major components of the application's interface.
 *
 * <p>This class implements the Runnable interface to enable launching the UI in a separate thread.</p>
 *
 * @author Ugnius Teišerskis
 */
public class EditorGUI
        implements Runnable {

    /** The main application window */
    private JFrame frame;

    /** Panel for displaying and editing text */
    private EditorPanel editorPanel;

    /** Panel with editor controls */
    private ControlPanel controlPanel;

    /** Panel for displaying status information */
    private StatusPanel statusPanel;

    /** Manager for the editor instances */
    private EditorManager editorManager;

    /**
     * Displays the GUI by making the main frame visible.
     * This method is called when the GUI is run in a separate thread.
     */
    @Override
    public void run() {
        frame.setVisible(true);
    }

    /**
     * Constructs a new EditorGUI instance.
     * Initializes the editor manager and creates the UI components.
     */
    public EditorGUI() {
        this.editorManager = new EditorManager();

        createGUI();
    }

    /**
     * Creates and arranges the UI components of the application.
     */
    private void createGUI() {
        frame = new JFrame("Text Editor Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 600);

        statusPanel = new StatusPanel();
        editorPanel = new EditorPanel(editorManager);
        controlPanel = new ControlPanel(editorManager, editorPanel, statusPanel);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(controlPanel, BorderLayout.NORTH);
        mainPanel.add(editorPanel, BorderLayout.CENTER);
        mainPanel.add(statusPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
    }
}