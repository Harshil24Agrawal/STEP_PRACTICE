import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;


public class UseCase6PalindromeCheckerApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Input : ");
        String input = scanner.nextLine();

        Queue<Character> queue = new LinkedList<>();
        Stack<Character> stack = new Stack<>();

        for (char c : input.toCharArray()) {
            queue.add(c);
            stack.push(c);
        }

        boolean isPalindrome = true;

        while (!queue.isEmpty()) {
            if (!queue.remove().equals(stack.pop())) {
                isPalindrome = false;
                break;
            }
        }

        System.out.println("Is Palindrome? : " + isPalindrome);

        scanner.close();
    }
}