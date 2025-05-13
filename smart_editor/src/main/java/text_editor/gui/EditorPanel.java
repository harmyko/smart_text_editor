package main.java.text_editor.gui;

import main.java.text_editor.editors.*;
import main.java.text_editor.factory.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Handles the text editing area and keyboard input.
 */
public class EditorPanel extends JPanel {
    private JTextArea textArea;
    private EditorManager editorManager;

    public EditorPanel(EditorManager editorManager) {
        this.editorManager = editorManager;

        setLayout(new BorderLayout());
        createTextArea();
    }

    private void createTextArea() {
        // Create text area with key listeners
        textArea = new JTextArea();
        textArea.setMargin(new Insets(10, 15, 10, 15));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font(textArea.getFont().getName(), Font.PLAIN, 14));

        // Set up key listeners for text input
        setupKeyListeners();

        // Add text area to a scroll pane
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(scrollPane, BorderLayout.CENTER);
    }

    private void setupKeyListeners() {
        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                // Consume the event to prevent default behavior
                e.consume();

                char c = e.getKeyChar();

                // Only handle regular character input here
                if (!Character.isISOControl(c)) {
                    editorManager.getCurrentEditor().addText(c);
                    updateDisplay();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    e.consume();

                    // Ctrl+Backspace = remove word
                    if (e.isControlDown()) {
                        editorManager.getCurrentEditor().removeWord();
                    } else {
                        // Regular Backspace = remove last character
                        editorManager.getCurrentEditor().removeLastCharacter();
                    }
                    updateDisplay();
                }
            }
        });
    }

    /**
     * Updates the displayed text and sets caret position
     */
    public void updateDisplay() {
        // Get current text from editor
        String currentText = editorManager.getCurrentEditor().toString();

        // Update the text area
        textArea.setText(currentText);

        // Always position cursor at the end
        textArea.setCaretPosition(currentText.length());
    }
}