import java.util.Scanner;


public class UseCase10PalindromeCheckerApp {


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Input : ");
        String input = scanner.nextLine();


        String normalized = input.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();

        boolean isPalindrome = checkPalindrome(normalized);

        System.out.println("Is Palindrome? : " + isPalindrome);

        scanner.close();
    }


    private static boolean checkPalindrome(String normalized) {
        if (normalized.isEmpty()) return true;

        for (int i = 0; i < normalized.length() / 2; i++) {
            if (normalized.charAt(i) != normalized.charAt(normalized.length() - 1 - i)) {
                return false;
            }
        }
        return true;
    }
}