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
        statusLabel = new JLabel("Ready");
        statusLabel.setFont(new Font(statusLabel.getFont().getName(), Font.PLAIN, 11));

        // Create progress bar (smaller size)
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(new Dimension(150, 15));

        // Create a panel for the status components
        JPanel statusBarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        statusBarPanel.add(statusLabel);
        statusBarPanel.add(progressBar);

        add(statusBarPanel, BorderLayout.WEST);
    }

    /**
     * Updates the status text and progress bar state
     */
    public void setStatus(String text, boolean indeterminate) {
        statusLabel.setText(text);
        progressBar.setIndeterminate(indeterminate);

        if (!indeterminate) {
            progressBar.setValue(0);
        }
    }

    /**
     * Updates status with specific progress value
     */
    public void setStatusWithProgress(String text, int progressValue, boolean indeterminate) {
        statusLabel.setText(text);
        progressBar.setIndeterminate(indeterminate);

        if (!indeterminate) {
            progressBar.setValue(progressValue);
        }
    }

    /**
     * Resets the status display to default state
     */
    public void resetStatus() {
        statusLabel.setText("Ready");
        progressBar.setIndeterminate(false);
        progressBar.setValue(0);
    }
}