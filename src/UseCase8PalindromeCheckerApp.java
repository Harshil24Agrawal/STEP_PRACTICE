import java.util.Scanner;
import java.util.LinkedList;


public class UseCase8PalindromeCheckerApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input : ");
        String input = scanner.nextLine();

        LinkedList<Character> list = new LinkedList<>();
        for (char c : input.toCharArray()) {
            list.add(c);
        }

        boolean isPalindrome = checkLinkedListPalindrome(list);

        System.out.println("Is Palindrome? : " + isPalindrome);
        scanner.close();
    }


    private static boolean checkLinkedListPalindrome(LinkedList<Character> list) {
        while (list.size() > 1) {
            if (!list.removeFirst().equals(list.removeLast())) {
                return false;
            }
        }
        return true;
    }
}