import java.util.*;

interface PalindromeStrategy {
    boolean isPalindrome(String input);
}


class StackStrategy implements PalindromeStrategy {
    @Override
    public boolean isPalindrome(String input) {
        String clean = input.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        Stack<Character> stack = new Stack<>();

        for (char c : clean.toCharArray()) {
            stack.push(c);
        }

        StringBuilder reversed = new StringBuilder();
        while (!stack.isEmpty()) {
            reversed.append(stack.pop());
        }

        return clean.equals(reversed.toString());
    }
}


class DequeStrategy implements PalindromeStrategy {
    @Override
    public boolean isPalindrome(String input) {
        String clean = input.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        Deque<Character> deque = new LinkedList<>();

        for (char c : clean.toCharArray()) {
            deque.addLast(c);
        }

        while (deque.size() > 1) {
            if (!deque.removeFirst().equals(deque.removeLast())) {
                return false;
            }
        }
        return true;
    }
}


class PalindromeChecker {
    private PalindromeStrategy strategy;

    public void setStrategy(PalindromeStrategy strategy) {
        this.strategy = strategy;
    }

    public boolean check(String text) {
        if (strategy == null) {
            System.out.println("Error: No algorithm selected.");
            return false;
        }
        return strategy.isPalindrome(text);
    }
}

public class PalindromeApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PalindromeChecker app = new PalindromeChecker();

        System.out.print("Enter a string: ");
        String userInput = scanner.nextLine();

        System.out.println("\n--- Testing with Stack Strategy ---");
        app.setStrategy(new StackStrategy());
        System.out.println("Result: " + app.check(userInput));

        System.out.println("\n--- Testing with Deque Strategy ---");
        app.setStrategy(new DequeStrategy());
        System.out.println("Result: " + app.check(userInput));

        scanner.close();
    }
}