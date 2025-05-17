package main.java.text_editor.gui;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EditorPanel extends JPanel {
    private JTextArea textArea;
    private EditorManager editorManager;
    private boolean updatingFromEditor = false;

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
        setupCaretListener();

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(scrollPane, BorderLayout.CENTER);
    }

    private void setupCaretListener() {
        textArea.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                if (!updatingFromEditor) {
                    editorManager.getCurrentEditor().setCaretPosition(e.getDot());
                }
            }
        });
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
                        // Ctrl+Backspace -> remove word at caret
                        editorManager.getCurrentEditor().removeWord();
                    } else {
                        // Regular Backspace -> remove character before caret
                        editorManager.getCurrentEditor().removeLastCharacter();
                    }
                    updateDisplay();
                }

                else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume();
                    editorManager.getCurrentEditor().addText('\n');
                    updateDisplay();
                }

                else if (e.getKeyCode() == KeyEvent.VK_LEFT ||
                        e.getKeyCode() == KeyEvent.VK_RIGHT ||
                        e.getKeyCode() == KeyEvent.VK_UP ||
                        e.getKeyCode() == KeyEvent.VK_DOWN) {
                }

                else {
                    int caretPosition = textArea.getCaretPosition();
                    editorManager.getCurrentEditor().setCaretPosition(caretPosition);
                }
            }
        });

        textArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int caretPosition = textArea.getCaretPosition();
                editorManager.getCurrentEditor().setCaretPosition(caretPosition);
            }
        });
    }

    public void updateDisplay() {
        updatingFromEditor = true;
        String currentText = editorManager.getCurrentEditor().toString();
        textArea.setText(currentText);

        int caretPos = editorManager.getCurrentEditor().getCaretPosition();
        if (caretPos >= 0 && caretPos <= currentText.length()) {
            textArea.setCaretPosition(caretPos);
        } else {
            textArea.setCaretPosition(currentText.length());
        }
        updatingFromEditor = false;
    }
}