package main.java.text_editor.gui;

import javax.swing.*;
import java.awt.*;

/**
 * The StatusPanel class represents the GUI component displaying status information.
 * It shows text status messages and progress indicators for operations.
 *
 * @author Ugnius Teišerskis
 */
public class StatusPanel
        extends JPanel {

    /** Label for displaying status messages */
    private JLabel statusLabel;

    /** Progress bar for indicating operation progress */
    private JProgressBar progressBar;

    /**
     * Constructs a new StatusPanel.
     * Initializes the status components with default values.
     */
    public StatusPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));

        createStatusComponents();
    }

    /**
     * Creates and configures the status display components.
     */
    private void createStatusComponents() {
        statusLabel = new JLabel("Editor status: Ready");
        statusLabel.setFont(new Font(statusLabel.getFont().getName(), Font.PLAIN, 11));

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(new Dimension(150, 15));

        JPanel statusTextPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        statusTextPanel.add(statusLabel);

        JPanel progressBarPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        progressBarPanel.add(progressBar);

        add(statusTextPanel, BorderLayout.WEST);
        add(progressBarPanel, BorderLayout.EAST);
    }

    /**
     * Sets the status text and indeterminate state of the progress bar.
     *
     * @param text the status text to display
     * @param indeterminate true to set the progress bar to indeterminate mode, false otherwise
     */
    public void setStatus(String text, boolean indeterminate) {
        statusLabel.setText("Editor status: " + text);
        progressBar.setIndeterminate(indeterminate);

        if (!indeterminate) {
            progressBar.setValue(0);
        }
    }

    /**
     * Sets the status text with a specific progress value.
     *
     * @param text the status text to display
     * @param progressValue the progress value (0-100)
     * @param indeterminate true to set the progress bar to indeterminate mode, false otherwise
     */
    public void setStatusWithProgress(String text, int progressValue, boolean indeterminate) {
        statusLabel.setText("Editor status: " + text);
        progressBar.setIndeterminate(indeterminate);

        if (!indeterminate) {
            progressBar.setValue(progressValue);
        }
    }

    /**
     * Resets the status display to the default "Ready" state.
     */
    public void resetStatus() {
        statusLabel.setText("Editor status: Ready");
        progressBar.setIndeterminate(false);
        progressBar.setValue(0);
    }
}