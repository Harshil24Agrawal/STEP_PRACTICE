import java.util.Scanner;

/**
 * UC11: Object-Oriented Palindrome Service
 * Ensure this file is saved as: UseCase11PalindromeCheckerApp.java
 */
public class Palindrome {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Palindrome1 palindromeService = new Palindrome1();

        System.out.print("Input : ");
        String userInput = scanner.nextLine();

        long startTime = System.nanoTime();
        boolean isPalindrome = palindromeService.checkPalindrome(userInput);
        long endTime = System.nanoTime();

        System.out.println("Is Palindrome? : " + isPalindrome);
        System.out.println("Execution Time : " + (endTime - startTime) + " ns");

        scanner.close();
    }
}

/**
 * Service class that contains palindrome logic.
 * This class is NOT public, so it can stay in the same file.
 */
class Palindrome1 {
    public boolean checkPalindrome(String input) {
        if (input == null) return false;

        String cleanInput = input.toLowerCase();
        int start = 0;
        int end = cleanInput.length() - 1;

        while (start < end) {
            if (cleanInput.charAt(start) != cleanInput.charAt(end)) {
                return false;
            }
            start++;
            end--;
        }
        return true;
    }
}