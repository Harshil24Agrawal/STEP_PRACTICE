import java.util.*;

/**
 * ========================================================
 * Problem 4: Plagiarism Detection System
 * ========================================================
 * Concepts: String hashing, n-grams, frequency counting, similarity scoring
 */
public class Problem4_PlagiarismDetector {

    private static final int N_GRAM_SIZE = 5;
    private static final double PLAGIARISM_THRESHOLD = 50.0;
    private static final double SUSPICIOUS_THRESHOLD = 10.0;

    // n-gram -> Set of document IDs
    private Map<String, Set<String>> ngramIndex = new HashMap<>();

    // docId -> list of its n-grams
    private Map<String, List<String>> documentNgrams = new HashMap<>();

    /** Index a document by extracting and storing its n-grams */
    public void indexDocument(String docId, String content) {
        String[] words = content.toLowerCase().split("\\s+");
        List<String> ngrams = extractNgrams(words);
        documentNgrams.put(docId, ngrams);

        for (String ngram : ngrams) {
            ngramIndex.computeIfAbsent(ngram, k -> new HashSet<>()).add(docId);
        }
        System.out.println("Indexed \"" + docId + "\" -> " + ngrams.size() + " n-grams extracted");
    }

    /** Extract n-grams from word array */
    private List<String> extractNgrams(String[] words) {
        List<String> ngrams = new ArrayList<>();
        for (int i = 0; i <= words.length - N_GRAM_SIZE; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = i; j < i + N_GRAM_SIZE; j++) {
                if (j > i) sb.append(" ");
                sb.append(words[j]);
            }
            ngrams.add(sb.toString());
        }
        return ngrams;
    }

    /** Analyze a document against all indexed documents */
    public void analyzeDocument(String docId) {
        List<String> targetNgrams = documentNgrams.get(docId);
        if (targetNgrams == null) {
            System.out.println("Document not indexed: " + docId);
            return;
        }

        System.out.println("\nanalyzeDocument(\"" + docId + "\")");
        System.out.println("-> Extracted " + targetNgrams.size() + " n-grams");

        // Count matches per document
        Map<String, Integer> matchCounts = new HashMap<>();
        for (String ngram : targetNgrams) {
            Set<String> docs = ngramIndex.getOrDefault(ngram, Collections.emptySet());
            for (String otherDoc : docs) {
                if (!otherDoc.equals(docId)) {
                    matchCounts.merge(otherDoc, 1, Integer::sum);
                }
            }
        }

        // Report results sorted by match count
        matchCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(entry -> {
                    double similarity = (entry.getValue() * 100.0) / targetNgrams.size();
                    String verdict = similarity >= PLAGIARISM_THRESHOLD ? "(PLAGIARISM DETECTED)" :
                            similarity >= SUSPICIOUS_THRESHOLD ? "(suspicious)" : "(acceptable)";
                    System.out.printf("-> Found %d matching n-grams with \"%s\"%n",
                            entry.getValue(), entry.getKey());
                    System.out.printf("-> Similarity: %.1f%% %s%n", similarity, verdict);
                });
    }

    public static void main(String[] args) {
        Problem4_PlagiarismDetector detector = new Problem4_PlagiarismDetector();

        System.out.println("=== Plagiarism Detection System ===");
        System.out.println();

        // Sample documents
        String essay089 = "the quick brown fox jumps over the lazy dog near the river bank " +
                "and the dog barked loudly at the fox running away into the forest quickly";
        String essay092 = "the quick brown fox jumps over the lazy dog near the river bank " +
                "and then the fox ran into the deep dark forest where no one could find it easily now";
        String essay123 = "the quick brown fox jumps over the lazy dog near the river bank " +
                "and the dog barked loudly at the fox running away into the forest quickly this is copied text";

        // Index documents
        System.out.println("--- Indexing Documents ---");
        detector.indexDocument("essay_089.txt", essay089);
        detector.indexDocument("essay_092.txt", essay092);
        detector.indexDocument("essay_123.txt", essay123);

        // Analyze essay_123 against others
        System.out.println("\n--- Analysis Results ---");
        detector.analyzeDocument("essay_123.txt");
    }
}
