import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;


public class UseCase7PalindromeCheckerApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Input : ");
        String input = scanner.nextLine();

        Deque<Character> deque = new ArrayDeque<>();

        for (char c : input.toCharArray()) {
            deque.addLast(c);
        }

        boolean isPalindrome = true;

        while (deque.size() > 1) {
            if (!deque.removeFirst().equals(deque.removeLast())) {
                isPalindrome = false;
                break;
            }
        }

        System.out.println("Is Palindrome? : " + isPalindrome);

        scanner.close();
    }
}