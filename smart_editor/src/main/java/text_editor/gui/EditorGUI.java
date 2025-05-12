package main.java.text_editor.gui;

import main.java.text_editor.editors.*;
import main.java.text_editor.factory.*;
import main.java.text_editor.serialization.*;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class EditorGUI implements Runnable {
    private JFrame frame;
    private JTextArea textArea;
    private JButton transformButton;
    private JButton saveButton;
    private JButton loadButton;
    private JComboBox<String> editorTypeComboBox;
    private JProgressBar progressBar;
    private JLabel statusLabel;

    private Editor currentEditor;
    private EditorFactory spellCheckFactory;
    private EditorFactory translateFactory;

    private static final String SOURCE_DICTIONARY_PATH = "smart_editor/src/main/resources/source_dictionary.txt";
    private static final String TARGET_DICTIONARY_PATH = "smart_editor/src/main/resources/target_dictionary.txt";
    private static final String RESOURCE_PATH = "smart_editor/src/main/resources/";

    public EditorGUI() {
        initializeFactories();
        createGUI();
    }

    @Override
    public void run() {
        frame.setVisible(true);
    }

    private void initializeFactories() {
        spellCheckFactory = new SpellCheckEditorFactory();
        translateFactory = new TranslateEditorFactory();

        currentEditor = spellCheckFactory.createEditor();

        if (currentEditor instanceof SpellCheckEditor) {
            ArrayList<String> words = readWordsFromFile(SOURCE_DICTIONARY_PATH);
            ((SpellCheckEditor) currentEditor).setDictionary(words);
        } else if (currentEditor instanceof TranslateEditor) {
            ArrayList<String> sourceWords = readWordsFromFile(SOURCE_DICTIONARY_PATH);
            ArrayList<String> targetWords = readWordsFromFile(TARGET_DICTIONARY_PATH);
            ((TranslateEditor) currentEditor).createTranslationMap(sourceWords, targetWords);
        }
    }

    private void createGUI() {
        frame = new JFrame("Text Editor Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 450);

        JPanel mainPanel = new JPanel(new BorderLayout());

        textArea = new JTextArea();
        textArea.setMargin(new Insets(5, 10, 5, 10));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel controlPanel = new JPanel(new FlowLayout());

        String[] editorTypes = {"SpellCheck Editor", "Translate Editor"};
        editorTypeComboBox = new JComboBox<>(editorTypes);
        editorTypeComboBox.addActionListener(e -> {
            String selectedType = (String) editorTypeComboBox.getSelectedItem();
            if ("SpellCheck Editor".equals(selectedType)) {
                currentEditor = spellCheckFactory.createEditor();
                if (currentEditor instanceof SpellCheckEditor) {
                    ((SpellCheckEditor) currentEditor).setDictionary(readWordsFromFile(SOURCE_DICTIONARY_PATH));
                }
                transformButton.setText("Spell Check");
            } else {
                currentEditor = translateFactory.createEditor();
                if (currentEditor instanceof TranslateEditor) {
                    ArrayList<String> sourceWords = readWordsFromFile(SOURCE_DICTIONARY_PATH);
                    ArrayList<String> targetWords = readWordsFromFile(TARGET_DICTIONARY_PATH);
                    ((TranslateEditor) currentEditor).createTranslationMap(sourceWords, targetWords);
                }
                transformButton.setText("Translate");
            }
        });

        transformButton = new JButton("Spell Check");
        transformButton.addActionListener(e -> {
            String text = textArea.getText();

            currentEditor = resetEditor();
            currentEditor.addText(text);

            Thread transformThread = new Thread(() -> {
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("Transforming text...");
                    progressBar.setIndeterminate(true);
                    transformButton.setEnabled(false);
                });

                try {
                    Thread.sleep(500);
                    currentEditor.transform();

                    SwingUtilities.invokeLater(() -> {
                        textArea.setText(currentEditor.toString());
                        statusLabel.setText("Text transformed successfully ");
                        progressBar.setIndeterminate(false);
                        progressBar.setValue(100);
                        transformButton.setEnabled(true);

                        Timer timer = new Timer(2000, event -> {
                            progressBar.setValue(0);
                            statusLabel.setText("Ready ");
                        });
                        timer.setRepeats(false);
                        timer.start();
                    });
                } catch (Exception ex) {
                    SwingUtilities.invokeLater(() -> {
                        statusLabel.setText("Error: " + ex.getMessage());
                        progressBar.setIndeterminate(false);
                        progressBar.setValue(0);
                        transformButton.setEnabled(true);
                    });
                    ex.printStackTrace();
                }
            });

            transformThread.start();
        });

        saveButton = new JButton("Save Editor");
        saveButton.addActionListener(e -> saveEditorState());

        loadButton = new JButton("Load Editor");
        loadButton.addActionListener(e -> loadEditorState());

        controlPanel.add(editorTypeComboBox);
        controlPanel.add(transformButton);
        controlPanel.add(saveButton);
        controlPanel.add(loadButton);

        JPanel statusPanel = new JPanel(new BorderLayout());
        statusLabel = new JLabel("Ready");
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);

        JPanel paddedLabelPanel = new JPanel(new BorderLayout());
        paddedLabelPanel.add(statusLabel, BorderLayout.CENTER);
        paddedLabelPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10)); // <-- 10px right padding

        statusPanel.add(paddedLabelPanel, BorderLayout.WEST);
        statusPanel.add(progressBar, BorderLayout.CENTER);
        statusPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.NORTH);
        mainPanel.add(statusPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
    }

    private Editor resetEditor() {
        String selectedType = (String) editorTypeComboBox.getSelectedItem();
        if ("SpellCheck Editor".equals(selectedType)) {
            Editor editor = spellCheckFactory.createEditor();
            if (editor instanceof SpellCheckEditor) {
                ((SpellCheckEditor) editor).setDictionary(readWordsFromFile(SOURCE_DICTIONARY_PATH));
            }
            return editor;
        } else {
            Editor editor = translateFactory.createEditor();
            if (editor instanceof TranslateEditor) {
                ArrayList<String> sourceWords = readWordsFromFile(SOURCE_DICTIONARY_PATH);
                ArrayList<String> targetWords = readWordsFromFile(TARGET_DICTIONARY_PATH);
                ((TranslateEditor) editor).createTranslationMap(sourceWords, targetWords);
            }
            return editor;
        }
    }

    private void saveEditorState() {
        final Editor editorToSave = currentEditor;
        final String editorType = editorTypeComboBox.getSelectedItem().toString();
        final String fileName = RESOURCE_PATH + editorType.replace(" ", "") + ".ser";

        Thread saveThread = new Thread(() -> {
            SwingUtilities.invokeLater(() -> {
                statusLabel.setText("Saving editor state...");
                progressBar.setIndeterminate(true);
                saveButton.setEnabled(false);
            });

            try {
                Thread.sleep(1000);

                File resourceDir = new File(RESOURCE_PATH);
                if (!resourceDir.exists()) {
                    resourceDir.mkdirs();
                }

                EditorSerializer.saveEditor(editorToSave, fileName);

                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("Editor state saved successfully to " + fileName);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(100);
                    saveButton.setEnabled(true);

                    Timer timer = new Timer(3000, event -> {
                        progressBar.setValue(0);
                        statusLabel.setText("Ready");
                    });
                    timer.setRepeats(false);
                    timer.start();
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("Error saving editor: " + e.getMessage());
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(0);
                    saveButton.setEnabled(true);

                    JOptionPane.showMessageDialog(frame, "Error saving editor: " + e.getMessage(),
                            "Save Error", JOptionPane.ERROR_MESSAGE);
                });
                e.printStackTrace();
            }
        });

        saveThread.start();
    }

    private void loadEditorState() {
        final String editorType = editorTypeComboBox.getSelectedItem().toString();
        final String fileName = RESOURCE_PATH + editorType.replace(" ", "") + ".ser";

        Thread loadThread = new Thread(() -> {
            SwingUtilities.invokeLater(() -> {
                statusLabel.setText("Loading editor state...");
                progressBar.setIndeterminate(true);
                loadButton.setEnabled(false);
            });

            try {
                Thread.sleep(1000);

                final Editor loadedEditor = EditorSerializer.loadEditor(fileName);

                SwingUtilities.invokeLater(() -> {
                    if ((loadedEditor instanceof SpellCheckEditor && "SpellCheck Editor".equals(editorType)) ||
                            (loadedEditor instanceof TranslateEditor && "Translate Editor".equals(editorType))) {

                        currentEditor = loadedEditor;
                        textArea.setText(currentEditor.toString());
                        statusLabel.setText("Editor loaded successfully from " + fileName);
                    } else {
                        statusLabel.setText("Loaded editor type doesn't match selected type");
                        JOptionPane.showMessageDialog(frame, "Loaded editor type doesn't match selected type",
                                "Load Error", JOptionPane.ERROR_MESSAGE);
                    }

                    progressBar.setIndeterminate(false);
                    progressBar.setValue(100);
                    loadButton.setEnabled(true);

                    // Reset progress bar after a delay
                    Timer timer = new Timer(3000, event -> {
                        progressBar.setValue(0);
                        statusLabel.setText("Ready");
                    });
                    timer.setRepeats(false);
                    timer.start();
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("Error loading editor: " + e.getMessage());
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(0);
                    loadButton.setEnabled(true);

                    JOptionPane.showMessageDialog(frame, "Error loading editor: " + e.getMessage(),
                            "Load Error", JOptionPane.ERROR_MESSAGE);
                });
                e.printStackTrace();
            }
        });

        loadThread.start();
    }

    public static ArrayList<String> readWordsFromFile(String filePath) {
        ArrayList<String> words = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                for (String word : line.split("\\s+")) {
                    if (!word.isEmpty()) {
                        words.add(word.toLowerCase());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Warning: " + e.getMessage());

            if (filePath.contains("source_dictionary")) {
                words.add("hello");
                words.add("world");
            } else if (filePath.contains("target_dictionary")) {
                words.add("labas");
                words.add("pasaulis");
            }
        }
        return words;
    }
}