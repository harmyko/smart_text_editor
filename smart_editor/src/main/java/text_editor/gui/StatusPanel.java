package main.java.text_editor.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Handles the status display and progress bar at the bottom of the GUI.
 */
public class StatusPanel extends JPanel {
    private JLabel statusLabel;
    private JProgressBar progressBar;

    public StatusPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));

        createStatusComponents();
    }

    private void createStatusComponents() {
        // Create status label
        statusLabel = new JLabel("Editor status: Ready");
        statusLabel.setFont(new Font(statusLabel.getFont().getName(), Font.PLAIN, 11));

        // Create progress bar (smaller size)
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(new Dimension(150, 15));

        // Create a panel for the status text (left aligned)
        JPanel statusTextPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        statusTextPanel.add(statusLabel);

        // Create a panel for the progress bar (right aligned)
        JPanel progressBarPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        progressBarPanel.add(progressBar);

        // Add components to the main panel
        add(statusTextPanel, BorderLayout.WEST);
        add(progressBarPanel, BorderLayout.EAST);
    }

    /**
     * Updates the status text and progress bar state
     */
    public void setStatus(String text, boolean indeterminate) {
        statusLabel.setText("Editor status: " + text);
        progressBar.setIndeterminate(indeterminate);

        if (!indeterminate) {
            progressBar.setValue(0);
        }
    }

    /**
     * Updates status with specific progress value
     */
    public void setStatusWithProgress(String text, int progressValue, boolean indeterminate) {
        statusLabel.setText("Editor status: " + text);
        progressBar.setIndeterminate(indeterminate);

        if (!indeterminate) {
            progressBar.setValue(progressValue);
        }
    }

    /**
     * Resets the status display to default state
     */
    public void resetStatus() {
        statusLabel.setText("Editor status: Ready");
        progressBar.setIndeterminate(false);
        progressBar.setValue(0);
    }
}