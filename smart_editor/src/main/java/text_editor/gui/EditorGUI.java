package main.java.text_editor.gui;

import javax.swing.*;
import java.awt.*;

public class EditorGUI implements Runnable {
    private JFrame frame;
    private EditorPanel editorPanel;
    private ControlPanel controlPanel;
    private StatusPanel statusPanel;

    private EditorManager editorManager;

    @Override
    public void run() {
        frame.setVisible(true);
    }

    public EditorGUI() {
        this.editorManager = new EditorManager();

        createGUI();
    }

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