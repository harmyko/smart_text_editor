package main.java.text_editor.prediction;

import java.util.*;

public class WordPredictor {
    private final Set<String> vocabulary;

    public WordPredictor(Collection<String> words) {
        this.vocabulary = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        this.vocabulary.addAll(words);
    }

    public List<String> predictWords(String prefix, int maxSuggestions) {
        if (prefix == null || prefix.trim().isEmpty()) {
            return new ArrayList<>();
        }

        List<String> predictions = new ArrayList<>();
        String lowerPrefix = prefix.toLowerCase();

        for (String word : vocabulary) {
            if (word.toLowerCase().startsWith(lowerPrefix)) {
                predictions.add(word);
                if (predictions.size() >= maxSuggestions) {
                    break;
                }
            }
        }

        return predictions;
    }

    public void addWord(String word) {
        if (word != null && !word.trim().isEmpty()) {
            vocabulary.add(word.trim());
        }
    }

    public void updateVocabulary(Collection<String> newWords) {
        vocabulary.clear();
        vocabulary.addAll(newWords);
    }
}