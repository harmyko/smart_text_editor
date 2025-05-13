package main.java.text_editor.gui;

import javax.swing.*;
import java.awt.*;

public class StatusPanel extends JPanel {
    private JLabel statusLabel;
    private JProgressBar progressBar;

    public StatusPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));

        createStatusComponents();
    }

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

    public void setStatus(String text, boolean indeterminate) {
        statusLabel.setText("Editor status: " + text);
        progressBar.setIndeterminate(indeterminate);

        if (!indeterminate) {
            progressBar.setValue(0);
        }
    }

    public void setStatusWithProgress(String text, int progressValue, boolean indeterminate) {
        statusLabel.setText("Editor status: " + text);
        progressBar.setIndeterminate(indeterminate);

        if (!indeterminate) {
            progressBar.setValue(progressValue);
        }
    }

    public void resetStatus() {
        statusLabel.setText("Editor status: Ready");
        progressBar.setIndeterminate(false);
        progressBar.setValue(0);
    }
}