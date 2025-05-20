package main.java.text_editor.gui;

import main.java.text_editor.editors.*;
import main.java.text_editor.serialization.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

/**
 * The ControlPanel class represents the GUI component containing editor controls.
 * It provides buttons and controls for transforming text, saving/loading editor state,
 * switching between editor types, and selecting dictionaries.
 *
 * @author Ugnius Teišerskis
 */
public class ControlPanel
        extends JPanel {

    /** Button for transforming text according to the current editor type */
    private JButton transformButton;

    /** Button for saving the current editor state */
    private JButton saveButton;

    /** Button for loading a saved editor state */
    private JButton loadButton;

    /** Button for choosing dictionaries for the current editor */
    private JButton chooseDictionaryButton;

    /** Dropdown for selecting the type of editor to use */
    private JComboBox<String> editorTypeComboBox;

    /** Manager for the editor instances */
    private EditorManager editorManager;

    /** Panel displaying the editor content */
    private EditorPanel editorPanel;

    /** Panel showing status information */
    private StatusPanel statusPanel;

    /**
     * Constructs a new ControlPanel with the specified components.
     *
     * @param editorManager the manager for editor instances
     * @param editorPanel the panel displaying the editor content
     * @param statusPanel the panel showing status information
     */
    public ControlPanel(EditorManager editorManager, EditorPanel editorPanel, StatusPanel statusPanel) {
        this.editorManager = editorManager;
        this.editorPanel = editorPanel;
        this.statusPanel = statusPanel;

        setLayout(new FlowLayout());
        createControls();
    }

    /**
     * Creates and initializes the control components like buttons and dropdowns.
     */
    private void createControls() {
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

        transformButton = new JButton("Spell Check");
        transformButton.addActionListener(e -> transformText());

        saveButton = new JButton("Save Editor");
        saveButton.addActionListener(e -> saveEditorState());

        loadButton = new JButton("Load Editor");
        loadButton.addActionListener(e -> loadEditorState());

        chooseDictionaryButton = new JButton("Choose Dictionary");
        chooseDictionaryButton.addActionListener(e -> chooseDictionary());

        add(editorTypeComboBox);
        add(transformButton);
        add(saveButton);
        add(loadButton);
        add(chooseDictionaryButton);
    }

    /**
     * Transforms the text in the current editor according to its transformation rules.
     * This operation runs on a separate thread to avoid blocking the UI.
     */
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

    /**
     * Saves the current editor state to a file selected by the user.
     * This operation runs on a separate thread to avoid blocking the UI.
     */
    private void saveEditorState() {
        final Editor editorToSave = editorManager.getCurrentEditor();
        final String editorType = editorTypeComboBox.getSelectedItem().toString();

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Editor State");

        fileChooser.setCurrentDirectory(new File(editorManager.getResourcePath()));
        fileChooser.setSelectedFile(new File(editorType.replace(" ", "") + ".ser"));

        int userSelection = fileChooser.showSaveDialog(SwingUtilities.getWindowAncestor(this));

        if (userSelection != JFileChooser.APPROVE_OPTION) {
            return;
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

    /**
     * Loads an editor state from a file selected by the user.
     * This operation runs on a separate thread to avoid blocking the UI.
     */
    private void loadEditorState() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load Editor State");
        fileChooser.setCurrentDirectory(new File(editorManager.getResourcePath()));

        int userSelection = fileChooser.showOpenDialog(SwingUtilities.getWindowAncestor(this));

        if (userSelection != JFileChooser.APPROVE_OPTION) {
            return;
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

    /**
     * Chooses the appropriate dictionary based on the current editor type.
     * Delegates to specific dictionary chooser methods.
     */
    private void chooseDictionary() {
        String selectedType = (String) editorTypeComboBox.getSelectedItem();

        if ("SpellCheck Editor".equals(selectedType)) {
            chooseSpellCheckDictionary();
        } else {
            chooseTranslateDictionaries();
        }
    }

    /**
     * Prompts the user to select a spell checking dictionary file.
     * This operation runs on a separate thread to avoid blocking the UI.
     */
    private void chooseSpellCheckDictionary() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Dictionary File");

        fileChooser.setCurrentDirectory(new File(editorManager.getResourcePath()));
        int userSelection = fileChooser.showOpenDialog(SwingUtilities.getWindowAncestor(this));

        if (userSelection != JFileChooser.APPROVE_OPTION) {
            return;
        }

        final String dictionaryPath = fileChooser.getSelectedFile().getAbsolutePath();

        Thread loadDictionaryThread = new Thread(() -> {
            SwingUtilities.invokeLater(() -> {
                statusPanel.setStatus("Loading dictionary...", true);
                chooseDictionaryButton.setEnabled(false);
            });

            try {
                Thread.sleep(500);

                ArrayList<String> words = EditorManager.readWordsFromFile(dictionaryPath);

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

    /**
     * Prompts the user to select source and target dictionary files for translation.
     * This operation runs on a separate thread to avoid blocking the UI.
     */
    private void chooseTranslateDictionaries() {
        JFileChooser sourceFileChooser = new JFileChooser();
        sourceFileChooser.setDialogTitle("Select Source Dictionary File");
        sourceFileChooser.setCurrentDirectory(new File(editorManager.getResourcePath()));

        int sourceSelection = sourceFileChooser.showOpenDialog(SwingUtilities.getWindowAncestor(this));

        if (sourceSelection != JFileChooser.APPROVE_OPTION) {
            return;
        }

        final String sourceDictionaryPath = sourceFileChooser.getSelectedFile().getAbsolutePath();

        JFileChooser targetFileChooser = new JFileChooser();
        targetFileChooser.setDialogTitle("Select Target Dictionary File");
        targetFileChooser.setCurrentDirectory(new File(editorManager.getResourcePath()));

        int targetSelection = targetFileChooser.showOpenDialog(SwingUtilities.getWindowAncestor(this));

        if (targetSelection != JFileChooser.APPROVE_OPTION) {
            return;
        }

        final String targetDictionaryPath = targetFileChooser.getSelectedFile().getAbsolutePath();

        Thread loadDictionariesThread = new Thread(() -> {
            SwingUtilities.invokeLater(() -> {
                statusPanel.setStatus("Loading dictionaries...", true);
                chooseDictionaryButton.setEnabled(false);
            });

            try {
                Thread.sleep(500);

                ArrayList<String> sourceWords = EditorManager.readWordsFromFile(sourceDictionaryPath);
                ArrayList<String> targetWords = EditorManager.readWordsFromFile(targetDictionaryPath);

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