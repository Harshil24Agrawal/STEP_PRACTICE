import java.util.*;
import java.util.stream.*;

/**
 * ========================================================
 * Problem 7: Autocomplete System for Search Engine
 * ========================================================
 * Concepts: HashMap frequency storage, prefix matching, top-K with min-heap
 */
public class Problem7_AutocompleteSystem {

    // query -> frequency count
    private Map<String, Integer> queryFrequency = new HashMap<>();

    private static final int TOP_K = 10;

    /** Add or update a search query frequency */
    public void addQuery(String query, int frequency) {
        queryFrequency.merge(query.toLowerCase(), frequency, Integer::sum);
    }

    /** Record a new search - increments frequency */
    public void updateFrequency(String query) {
        int old = queryFrequency.getOrDefault(query.toLowerCase(), 0);
        queryFrequency.merge(query.toLowerCase(), 1, Integer::sum);
        int newFreq = queryFrequency.get(query.toLowerCase());
        System.out.println("updateFrequency(\"" + query + "\") -> Frequency: " +
                old + " -> " + newFreq +
                (newFreq <= 3 ? " (trending)" : ""));
    }

    /** Search for top-K suggestions matching a prefix */
    public List<String> search(String prefix) {
        String lPrefix = prefix.toLowerCase();

        // Use min-heap to efficiently find top K
        PriorityQueue<Map.Entry<String, Integer>> minHeap =
                new PriorityQueue<>(Comparator.comparingInt(Map.Entry::getValue));

        for (Map.Entry<String, Integer> entry : queryFrequency.entrySet()) {
            if (entry.getKey().startsWith(lPrefix)) {
                minHeap.offer(entry);
                if (minHeap.size() > TOP_K) minHeap.poll(); // remove lowest
            }
        }

        // Convert heap to sorted list (descending)
        List<Map.Entry<String, Integer>> results = new ArrayList<>(minHeap);
        results.sort(Map.Entry.<String, Integer>comparingByValue().reversed());
        return results.stream()
                .map(e -> "\"" + e.getKey() + "\" (" + String.format("%,d", e.getValue()) + " searches)")
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        Problem7_AutocompleteSystem ac = new Problem7_AutocompleteSystem();

        System.out.println("=== Autocomplete System for Search Engine ===");
        System.out.println();

        // Seed with realistic search queries
        ac.addQuery("java tutorial", 1_234_567);
        ac.addQuery("javascript", 987_654);
        ac.addQuery("java download", 456_789);
        ac.addQuery("java 21 features", 1);
        ac.addQuery("java spring boot", 345_210);
        ac.addQuery("java interview questions", 289_000);
        ac.addQuery("java vs python", 198_450);
        ac.addQuery("java certification", 156_700);
        ac.addQuery("java stream api", 143_200);
        ac.addQuery("java 17 lts", 98_500);
        ac.addQuery("javascript frameworks", 654_321);
        ac.addQuery("javascript tutorial", 543_210);

        // Search with prefix "jav"
        System.out.println("search(\"jav\") ->");
        List<String> results = ac.search("jav");
        for (int i = 0; i < results.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + results.get(i));
        }
        System.out.println();

        // Update frequency to simulate trending
        ac.updateFrequency("java 21 features");
        ac.updateFrequency("java 21 features");
        ac.updateFrequency("java 21 features");
    }
}