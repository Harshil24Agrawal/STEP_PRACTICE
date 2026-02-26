import java.util.Scanner;


public class UseCase9PalindromeCheckerApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Input : ");
        String userInput = scanner.nextLine();

        boolean result = check(userInput, 0, userInput.length() - 1);

        System.out.println("Is Palindrome? : " + result);

        scanner.close();
    }


    private static boolean check(String s, int start, int end) {
        // Base Condition: All characters matched or string of length 0/1
        if (start >= end) {
            return true;
        }

        if (s.charAt(start) != s.charAt(end)) {
            return false;
        }

        return check(s, start + 1, end - 1);
    }
}