package main.java.text_editor.gui;

import main.java.text_editor.editors.SpellCheckEditor;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * The EditorPanel class represents the GUI component displaying the editor content.
 * It handles text display, user input, and word prediction features.
 *
 * @author Ugnius Teišerskis
 */
public class EditorPanel
        extends JPanel {

    /** Text area component for displaying and editing text */
    private JTextArea textArea;

    /** Manager for the editor instances */
    private EditorManager editorManager;

    /** Flag to prevent infinite update loops when updating from the editor */
    private boolean updatingFromEditor = false;

    /** Popup menu for displaying word predictions */
    private JPopupMenu predictionPopup;

    /** Timer for delaying word prediction display */
    private javax.swing.Timer predictionTimer;

    /**
     * Constructs a new EditorPanel with the specified editor manager.
     *
     * @param editorManager the manager for editor instances
     */
    public EditorPanel(EditorManager editorManager) {
        this.editorManager = editorManager;

        setLayout(new BorderLayout());
        createTextArea();
        setupPredictionPopup();
    }

    /**
     * Creates and configures the text area component.
     */
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

    /**
     * Sets up the caret listener for tracking caret position changes.
     */
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

    /**
     * Sets up keyboard and mouse listeners for handling user input.
     */
    private void setupKeyListeners() {
        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                e.consume();

                char c = e.getKeyChar();

                if (!Character.isISOControl(c)) {
                    editorManager.getCurrentEditor().addText(c);
                    updateDisplay();

                    if (editorManager.getCurrentEditor() instanceof SpellCheckEditor) {
                        predictionTimer.restart();
                    }
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    e.consume();

                    if (e.isControlDown()) {
                        // Ctrl+Backspace -> remove word at caret
                        if (editorManager.getCurrentEditor().getCaretPosition() > 0) {
                            editorManager.getCurrentEditor().removeWord();
                        }
                    } else {
                        // Regular Backspace -> remove character before caret
                        if (editorManager.getCurrentEditor().getCaretPosition() > 0) {
                            editorManager.getCurrentEditor().removeLastCharacter();
                        }
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

    /**
     * Sets up the word prediction popup and its timer.
     */
    private void setupPredictionPopup() {
        predictionPopup = new JPopupMenu();
        predictionPopup.setFocusable(false);

        predictionTimer = new javax.swing.Timer(300, e -> showPredictions());
        predictionTimer.setRepeats(false);
    }

    /**
     * Shows word predictions based on the current word being typed.
     * The predictions are displayed in a popup menu near the caret position.
     */
    private void showPredictions() {
        if (!(editorManager.getCurrentEditor() instanceof SpellCheckEditor)) {
            hidePredictions();
            return;
        }

        SpellCheckEditor spellEditor = (SpellCheckEditor) editorManager.getCurrentEditor();
        List<String> predictions = spellEditor.getPredictions(5); // Show max 5 predictions

        if (predictions.isEmpty()) {
            hidePredictions();
            return;
        }

        predictionPopup.removeAll();

        for (String prediction : predictions) {
            JMenuItem item = new JMenuItem(prediction);
            item.addActionListener(e -> {
                insertPrediction(prediction);
                hidePredictions();
            });
            predictionPopup.add(item);
        }

        // Show popup near caret
        try {
            Rectangle caretRect = textArea.modelToView(textArea.getCaretPosition());
            predictionPopup.show(textArea, caretRect.x, caretRect.y + caretRect.height);
        } catch (Exception ex) {
            Point mousePos = textArea.getMousePosition();
            if (mousePos != null) {
                predictionPopup.show(textArea, mousePos.x, mousePos.y);
            }
        }
    }

    /**
     * Inserts a selected prediction word, replacing the current word being typed.
     *
     * @param prediction the prediction word to insert
     */
    private void insertPrediction(String prediction) {
        SpellCheckEditor spellEditor = (SpellCheckEditor) editorManager.getCurrentEditor();
        String currentWord = spellEditor.getCurrentWord();

        for (int i = 0; i < currentWord.length(); i++) {
            spellEditor.removeLastCharacter();
        }
        spellEditor.addText(prediction);
        updateDisplay();
    }

    /**
     * Hides the prediction popup if it is visible.
     */
    private void hidePredictions() {
        if (predictionPopup != null && predictionPopup.isVisible()) {
            predictionPopup.setVisible(false);
        }
    }

    /**
     * Updates the display to reflect the current state of the editor.
     * This method synchronizes the text area content with the editor content.
     */
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