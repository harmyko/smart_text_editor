package main.java.text_editor.gui;

import main.java.text_editor.editors.*;
import main.java.text_editor.factory.*;
import main.java.text_editor.serialization.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Contains the editor control buttons and type selection.
 */
public class ControlPanel extends JPanel {
    private JButton transformButton;
    private JButton saveButton;
    private JButton loadButton;
    private JComboBox<String> editorTypeComboBox;

    private EditorManager editorManager;
    private EditorPanel editorPanel;
    private StatusPanel statusPanel;

    public ControlPanel(EditorManager editorManager, EditorPanel editorPanel, StatusPanel statusPanel) {
        this.editorManager = editorManager;
        this.editorPanel = editorPanel;
        this.statusPanel = statusPanel;

        setLayout(new FlowLayout());
        createControls();
    }

    private void createControls() {
        // Create editor type dropdown
        String[] editorTypes = {"SpellCheck Editor", "Translate Editor"};
        editorTypeComboBox = new JComboBox<>(editorTypes);
        editorTypeComboBox.addActionListener(e -> {
            String selectedType = (String) editorTypeComboBox.getSelectedItem();
            if ("SpellCheck Editor".equals(selectedType)) {
                editorManager.switchToSpellCheckEditor();
                transformButton.setText("Spell Check");
            } else {
                editorManager.switchToTranslateEditor();
                transformButton.setText("Translate");
            }
            editorPanel.updateDisplay();
        });

        // Create control buttons
        transformButton = new JButton("Spell Check");
        transformButton.addActionListener(e -> transformText());

        saveButton = new JButton("Save Editor");
        saveButton.addActionListener(e -> saveEditorState());

        loadButton = new JButton("Load Editor");
        loadButton.addActionListener(e -> loadEditorState());

        // Add components to panel
        add(editorTypeComboBox);
        add(transformButton);
        add(saveButton);
        add(loadButton);
    }

    private void transformText() {
        Thread transformThread = new Thread(() -> {
            SwingUtilities.invokeLater(() -> {
                statusPanel.setStatus("Transforming text...", true);
                transformButton.setEnabled(false);
            });

            try {
                Thread.sleep(500);
                editorManager.getCurrentEditor().transform();

                SwingUtilities.invokeLater(() -> {
                    editorPanel.updateDisplay();
                    statusPanel.setStatusWithProgress("Text transformed successfully", 100, false);
                    transformButton.setEnabled(true);

                    Timer timer = new Timer(2000, event -> {
                        statusPanel.resetStatus();
                    });
                    timer.setRepeats(false);
                    timer.start();
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    statusPanel.setStatus("Error: " + ex.getMessage(), false);
                    transformButton.setEnabled(true);
                });
                ex.printStackTrace();
            }
        });

        transformThread.start();
    }

    private void saveEditorState() {
        final Editor editorToSave = editorManager.getCurrentEditor();
        final String editorType = editorTypeComboBox.getSelectedItem().toString();
        final String fileName = editorManager.getResourcePath() + editorType.replace(" ", "") + ".ser";

        Thread saveThread = new Thread(() -> {
            SwingUtilities.invokeLater(() -> {
                statusPanel.setStatus("Saving editor state...", true);
                saveButton.setEnabled(false);
            });

            try {
                Thread.sleep(1000);

                File resourceDir = new File(editorManager.getResourcePath());
                if (!resourceDir.exists()) {
                    resourceDir.mkdirs();
                }

                EditorSerializer.saveEditor(editorToSave, fileName);

                SwingUtilities.invokeLater(() -> {
                    statusPanel.setStatusWithProgress("Editor state saved successfully to " + fileName, 100, false);
                    saveButton.setEnabled(true);

                    Timer timer = new Timer(3000, event -> {
                        statusPanel.resetStatus();
                    });
                    timer.setRepeats(false);
                    timer.start();
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    statusPanel.setStatus("Error saving editor: " + e.getMessage(), false);
                    saveButton.setEnabled(true);

                    JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
                            "Error saving editor: " + e.getMessage(),
                            "Save Error", JOptionPane.ERROR_MESSAGE);
                });
                e.printStackTrace();
            }
        });

        saveThread.start();
    }

    private void loadEditorState() {
        final String editorType = editorTypeComboBox.getSelectedItem().toString();
        final String fileName = editorManager.getResourcePath() + editorType.replace(" ", "") + ".ser";

        Thread loadThread = new Thread(() -> {
            SwingUtilities.invokeLater(() -> {
                statusPanel.setStatus("Loading editor state...", true);
                loadButton.setEnabled(false);
            });

            try {
                Thread.sleep(1000);

                final Editor loadedEditor = EditorSerializer.loadEditor(fileName);

                SwingUtilities.invokeLater(() -> {
                    if ((loadedEditor instanceof SpellCheckEditor && "SpellCheck Editor".equals(editorType)) ||
                            (loadedEditor instanceof TranslateEditor && "Translate Editor".equals(editorType))) {

                        editorManager.setCurrentEditor(loadedEditor);
                        editorPanel.updateDisplay();
                        statusPanel.setStatusWithProgress("Editor loaded successfully from " + fileName, 100, false);
                    } else {
                        statusPanel.setStatus("Loaded editor type doesn't match selected type", false);
                        JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
                                "Loaded editor type doesn't match selected type",
                                "Load Error", JOptionPane.ERROR_MESSAGE);
                    }

                    loadButton.setEnabled(true);

                    // Reset progress bar after a delay
                    Timer timer = new Timer(3000, event -> {
                        statusPanel.resetStatus();
                    });
                    timer.setRepeats(false);
                    timer.start();
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    statusPanel.setStatus("Error loading editor: " + e.getMessage(), false);
                    loadButton.setEnabled(true);

                    JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
                            "Error loading editor: " + e.getMessage(),
                            "Load Error", JOptionPane.ERROR_MESSAGE);
                });
                e.printStackTrace();
            }
        });

        loadThread.start();
    }
}