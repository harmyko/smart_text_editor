package main.java.text_editor.gui;

import main.java.text_editor.editors.*;
import main.java.text_editor.factory.*;
import main.java.text_editor.serialization.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

/**
 * Contains the editor control buttons and type selection.
 */
public class ControlPanel extends JPanel {
    private JButton transformButton;
    private JButton saveButton;
    private JButton loadButton;
    private JButton chooseDictionaryButton;
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

        chooseDictionaryButton = new JButton("Choose Dictionary");
        chooseDictionaryButton.addActionListener(e -> chooseDictionary());

        // Add components to panel
        add(editorTypeComboBox);
        add(transformButton);
        add(saveButton);
        add(loadButton);
        add(chooseDictionaryButton);
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

        // Show file chooser dialog to select save location
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Editor State");
        fileChooser.setSelectedFile(new File(editorManager.getResourcePath() + editorType.replace(" ", "") + ".ser"));

        int userSelection = fileChooser.showSaveDialog(SwingUtilities.getWindowAncestor(this));

        if (userSelection != JFileChooser.APPROVE_OPTION) {
            return; // User cancelled the operation
        }

        final String fileName = fileChooser.getSelectedFile().getAbsolutePath();

        Thread saveThread = new Thread(() -> {
            SwingUtilities.invokeLater(() -> {
                statusPanel.setStatus("Saving editor state...", true);
                saveButton.setEnabled(false);
            });

            try {
                Thread.sleep(1000);

                File parentDir = new File(fileName).getParentFile();
                if (parentDir != null && !parentDir.exists()) {
                    parentDir.mkdirs();
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
        // Show file chooser dialog to select file to load
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load Editor State");
        fileChooser.setSelectedFile(new File(editorManager.getResourcePath()));

        int userSelection = fileChooser.showOpenDialog(SwingUtilities.getWindowAncestor(this));

        if (userSelection != JFileChooser.APPROVE_OPTION) {
            return; // User cancelled the operation
        }

        final String fileName = fileChooser.getSelectedFile().getAbsolutePath();
        final String editorType = editorTypeComboBox.getSelectedItem().toString();

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

    private void chooseDictionary() {
        String selectedType = (String) editorTypeComboBox.getSelectedItem();

        if ("SpellCheck Editor".equals(selectedType)) {
            // For SpellCheck, select a single dictionary file
            chooseSpellCheckDictionary();
        } else {
            // For Translate, select source and target dictionary files
            chooseTranslateDictionaries();
        }
    }

    private void chooseSpellCheckDictionary() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Dictionary File");

        int userSelection = fileChooser.showOpenDialog(SwingUtilities.getWindowAncestor(this));

        if (userSelection != JFileChooser.APPROVE_OPTION) {
            return; // User cancelled the operation
        }

        final String dictionaryPath = fileChooser.getSelectedFile().getAbsolutePath();

        Thread loadDictionaryThread = new Thread(() -> {
            SwingUtilities.invokeLater(() -> {
                statusPanel.setStatus("Loading dictionary...", true);
                chooseDictionaryButton.setEnabled(false);
            });

            try {
                Thread.sleep(500);

                // Read words from the selected file
                ArrayList<String> words = EditorManager.readWordsFromFile(dictionaryPath);

                // Update the current editor if it's a SpellCheckEditor
                Editor currentEditor = editorManager.getCurrentEditor();
                if (currentEditor instanceof SpellCheckEditor) {
                    ((SpellCheckEditor) currentEditor).setDictionary(words);

                    SwingUtilities.invokeLater(() -> {
                        statusPanel.setStatusWithProgress("Dictionary loaded successfully", 100, false);
                        chooseDictionaryButton.setEnabled(true);

                        Timer timer = new Timer(2000, event -> {
                            statusPanel.resetStatus();
                        });
                        timer.setRepeats(false);
                        timer.start();
                    });
                }
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    statusPanel.setStatus("Error loading dictionary: " + e.getMessage(), false);
                    chooseDictionaryButton.setEnabled(true);

                    JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
                            "Error loading dictionary: " + e.getMessage(),
                            "Dictionary Error", JOptionPane.ERROR_MESSAGE);
                });
                e.printStackTrace();
            }
        });

        loadDictionaryThread.start();
    }

    private void chooseTranslateDictionaries() {
        // First, select source dictionary
        JFileChooser sourceFileChooser = new JFileChooser();
        sourceFileChooser.setDialogTitle("Select Source Dictionary File");

        int sourceSelection = sourceFileChooser.showOpenDialog(SwingUtilities.getWindowAncestor(this));

        if (sourceSelection != JFileChooser.APPROVE_OPTION) {
            return; // User cancelled the operation
        }

        final String sourceDictionaryPath = sourceFileChooser.getSelectedFile().getAbsolutePath();

        // Then, select target dictionary
        JFileChooser targetFileChooser = new JFileChooser();
        targetFileChooser.setDialogTitle("Select Target Dictionary File");

        int targetSelection = targetFileChooser.showOpenDialog(SwingUtilities.getWindowAncestor(this));

        if (targetSelection != JFileChooser.APPROVE_OPTION) {
            return; // User cancelled the operation
        }

        final String targetDictionaryPath = targetFileChooser.getSelectedFile().getAbsolutePath();

        Thread loadDictionariesThread = new Thread(() -> {
            SwingUtilities.invokeLater(() -> {
                statusPanel.setStatus("Loading dictionaries...", true);
                chooseDictionaryButton.setEnabled(false);
            });

            try {
                Thread.sleep(500);

                // Read words from the selected files
                ArrayList<String> sourceWords = EditorManager.readWordsFromFile(sourceDictionaryPath);
                ArrayList<String> targetWords = EditorManager.readWordsFromFile(targetDictionaryPath);

                // Update the current editor if it's a TranslateEditor
                Editor currentEditor = editorManager.getCurrentEditor();
                if (currentEditor instanceof TranslateEditor) {
                    ((TranslateEditor) currentEditor).createTranslationMap(sourceWords, targetWords);

                    SwingUtilities.invokeLater(() -> {
                        statusPanel.setStatusWithProgress("Dictionaries loaded successfully", 100, false);
                        chooseDictionaryButton.setEnabled(true);

                        Timer timer = new Timer(2000, event -> {
                            statusPanel.resetStatus();
                        });
                        timer.setRepeats(false);
                        timer.start();
                    });
                }
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    statusPanel.setStatus("Error loading dictionaries: " + e.getMessage(), false);
                    chooseDictionaryButton.setEnabled(true);

                    JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
                            "Error loading dictionaries: " + e.getMessage(),
                            "Dictionary Error", JOptionPane.ERROR_MESSAGE);
                });
                e.printStackTrace();
            }
        });

        loadDictionariesThread.start();
    }
}