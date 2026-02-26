import java.util.Scanner;

/**
 * MAIN CLASS - UseCase10PalindromeCheckerApp
 * Use Case 10: Normalized Palindrome Validation
 */
public class UseCase10PalindromeCheckerApp {

    /**
     * Application entry point for UC10.
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Input : ");
        String input = scanner.nextLine();

        // Step 1: Normalization (UC10 Goal)
        // Removing spaces and symbols using Regular Expressions, then converting to lowercase
        String normalized = input.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();

        // Step 2: Apply validation logic
        boolean isPalindrome = checkPalindrome(normalized);

        // Output results
        System.out.println("Is Palindrome? : " + isPalindrome);

        scanner.close();
    }

    /**
     * Core logic to validate a palindrome after preprocessing.
     * @param normalized Preprocessed string
     * @return true if palindrome, false otherwise
     */
    private static boolean checkPalindrome(String normalized) {
        if (normalized.isEmpty()) return true;

        // Compare characters from both ends as per the hint
        for (int i = 0; i < normalized.length() / 2; i++) {
            // Compare symmetric characters
            if (normalized.charAt(i) != normalized.charAt(normalized.length() - 1 - i)) {
                return false;
            }
        }
        return true;
    }
}