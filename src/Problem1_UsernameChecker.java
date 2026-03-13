import java.util.*;

/**
 * ========================================================
 * Problem 1: Social Media Username Availability Checker
 * ========================================================
 * Concepts: Hash table basics, O(1) lookup, frequency counting
 */
public class Problem1_UsernameChecker {

    // username -> userId mapping
    private Map<String, Integer> registeredUsers = new HashMap<>();

    // username attempt frequency tracking
    private Map<String, Integer> attemptFrequency = new HashMap<>();

    private int nextUserId = 1;

    /** Register a username with a userId */
    public void registerUsername(String username) {
        registeredUsers.put(username, nextUserId++);
    }

    /** Check availability in O(1) - tracks attempt frequency */
    public boolean checkAvailability(String username) {
        attemptFrequency.merge(username, 1, Integer::sum);
        return !registeredUsers.containsKey(username);
    }

    /** Suggest alternatives if username is taken */
    public List<String> suggestAlternatives(String username) {
        List<String> suggestions = new ArrayList<>();
        suggestions.add(username + "1");
        suggestions.add(username + "2");
        suggestions.add(username.replace("_", "."));
        suggestions.add("_" + username + "_");
        // Filter out already taken suggestions
        suggestions.removeIf(s -> registeredUsers.containsKey(s));
        return suggestions;
    }

    /** Get the most attempted username */
    public String getMostAttempted() {
        return attemptFrequency.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(e -> e.getKey() + " (" + e.getValue() + " attempts)")
                .orElse("No attempts yet");
    }

    public static void main(String[] args) {
        Problem1_UsernameChecker checker = new Problem1_UsernameChecker();

        // Pre-register some users
        checker.registerUsername("john_doe");
        checker.registerUsername("admin");
        checker.registerUsername("admin");

        // Simulate many attempts on "admin"
        for (int i = 0; i < 10543; i++) checker.checkAvailability("admin");

        System.out.println("=== Username Availability Checker ===");
        System.out.println();

        System.out.println("checkAvailability(\"john_doe\") -> " +
                checker.checkAvailability("john_doe") + " (already taken)");
        System.out.println("checkAvailability(\"jane_smith\") -> " +
                checker.checkAvailability("jane_smith") + " (available)");
        System.out.println();

        System.out.println("suggestAlternatives(\"john_doe\") -> " +
                checker.suggestAlternatives("john_doe"));
        System.out.println();

        System.out.println("getMostAttempted() -> " + checker.getMostAttempted());
    }
}