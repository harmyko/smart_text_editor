package main.java.text_editor.prediction;

import java.util.*;

/**
 * The WordPredictor class provides word prediction functionality based on a vocabulary.
 * It suggests words that start with a given prefix.
 *
 * @author Ugnius Teišerskis
 */
public class WordPredictor {

    /** Set of words used for predictions, sorted case-insensitively */
    private final Set<String> vocabulary;

    /**
     * Constructs a WordPredictor with the specified vocabulary.
     *
     * @param words the collection of words to use as the vocabulary
     */
    public WordPredictor(Collection<String> words) {
        this.vocabulary = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        this.vocabulary.addAll(words);
    }

    /**
     * Predicts words that start with the given prefix.
     * The predictions are case-insensitive.
     *
     * @param prefix the prefix to match
     * @param maxSuggestions the maximum number of suggestions to return
     * @return a list of words that start with the prefix
     */
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

    /**
     * Adds a word to the vocabulary.
     * Empty or null words are ignored.
     *
     * @param word the word to add to the vocabulary
     */
    public void addWord(String word) {
        if (word != null && !word.trim().isEmpty()) {
            vocabulary.add(word.trim());
        }
    }

    /**
     * Updates the vocabulary with a new collection of words.
     * The previous vocabulary is cleared.
     *
     * @param newWords the new collection of words for the vocabulary
     */
    public void updateVocabulary(Collection<String> newWords) {
        vocabulary.clear();
        vocabulary.addAll(newWords);
    }
}