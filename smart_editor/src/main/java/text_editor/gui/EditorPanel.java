package main.java.text_editor.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class EditorPanel extends JPanel {
    private JTextArea textArea;
    private EditorManager editorManager;

    public EditorPanel(EditorManager editorManager) {
        this.editorManager = editorManager;

        setLayout(new BorderLayout());
        createTextArea();
    }

    private void createTextArea() {
        textArea = new JTextArea();
        textArea.setMargin(new Insets(10, 15, 10, 15));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font(textArea.getFont().getName(), Font.PLAIN, 14));

        setupKeyListeners();

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(scrollPane, BorderLayout.CENTER);
    }

    private void setupKeyListeners() {
        textArea.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                e.consume();

                char c = e.getKeyChar();

                if (!Character.isISOControl(c)) {
                    editorManager.getCurrentEditor().addText(c);
                    updateDisplay();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    e.consume();

                    if (e.isControlDown()) {
                        // Ctrl+Backspace -> remove word
                        editorManager.getCurrentEditor().removeWord();
                    } else {
                        // Regular Backspace -> remove last character
                        editorManager.getCurrentEditor().removeLastCharacter();
                    }
                    updateDisplay();
                }
            }
        });
    }

    public void updateDisplay() {
        String currentText = editorManager.getCurrentEditor().toString();
        textArea.setText(currentText);
        textArea.setCaretPosition(currentText.length());
    }
}