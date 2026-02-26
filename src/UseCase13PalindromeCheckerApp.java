public class UseCase13PalindromeCheckerApp {

    public static void main(String[] args) {
        // A long palindrome to make the CPU work a bit harder
        String input = "racecar".repeat(1000);
        int iterations = 10000;

        System.out.println("--- Palindrome Performance Comparison ---");
        System.out.println("Input Length: " + input.length() + " characters");
        System.out.println("Iterations: " + iterations + "\n");

        // 1. Two-Pointer Approach
        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) isTwoPointer(input);
        long durationTP = (System.nanoTime() - start) / iterations;
        System.out.println("Two-Pointer Avg:   " + durationTP + " ns");

        // 2. StringBuilder Approach
        start = System.nanoTime();
        for (int i = 0; i < iterations; i++) isStringBuilder(input);
        long durationSB = (System.nanoTime() - start) / iterations;
        System.out.println("StringBuilder Avg: " + durationSB + " ns");

        // 3. Recursive Approach (Note: Large strings may cause StackOverflow)
        // Using a smaller version for recursion safety
        String shortInput = "racecar".repeat(10);
        start = System.nanoTime();
        for (int i = 0; i < iterations; i++) isRecursive(shortInput, 0, shortInput.length() - 1);
        long durationRec = (System.nanoTime() - start) / iterations;
        System.out.println("Recursive Avg:     " + durationRec + " ns (on shorter input)");
    }

    public static boolean isTwoPointer(String s) {
        int left = 0, right = s.length() - 1;
        while (left < right) {
            if (s.charAt(left++) != s.charAt(right--)) return false;
        }
        return true;
    }

    public static boolean isStringBuilder(String s) {
        return s.equals(new StringBuilder(s).reverse().toString());
    }

    public static boolean isRecursive(String s, int l, int r) {
        if (l >= r) return true;
        if (s.charAt(l) != s.charAt(r)) return false;
        return isRecursive(s, l + 1, r - 1);
    }
}
