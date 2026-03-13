import java.util.*;

/**
 * ========================================================
 * Problem 9: Two-Sum Problem Variants for Financial Transactions
 * ========================================================
 * Concepts: HashMap complement lookup, O(1) lookup, duplicate detection, K-Sum
 */
public class Problem9_TwoSumTransactions {

    static class Transaction {
        int id;
        double amount;
        String merchant;
        String time;
        String accountId;

        Transaction(int id, double amount, String merchant, String time, String accountId) {
            this.id = id;
            this.amount = amount;
            this.merchant = merchant;
            this.time = time;
            this.accountId = accountId;
        }

        @Override
        public String toString() {
            return "{id:" + id + ", amount:" + amount + ", merchant:\"" + merchant + "\"}";
        }
    }

    private List<Transaction> transactions = new ArrayList<>();

    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    /**
     * Classic Two-Sum: Find all pairs summing to target - O(n)
     */
    public List<int[]> findTwoSum(double target) {
        List<int[]> result = new ArrayList<>();
        // complement -> transaction index
        Map<Double, Integer> seen = new HashMap<>();

        for (int i = 0; i < transactions.size(); i++) {
            double complement = target - transactions.get(i).amount;
            if (seen.containsKey(complement)) {
                result.add(new int[]{seen.get(complement), i});
            }
            seen.put(transactions.get(i).amount, i);
        }
        return result;
    }

    /**
     * Two-Sum with time window: Pairs within same hour
     */
    public List<int[]> findTwoSumWithTimeWindow(double target, int windowMinutes) {
        List<int[]> result = new ArrayList<>();
        for (int i = 0; i < transactions.size(); i++) {
            Map<Double, Integer> seen = new HashMap<>();
            seen.put(transactions.get(i).amount, i);
            for (int j = i + 1; j < transactions.size(); j++) {
                // Simple time check - parse HH:MM
                int timeI = parseMinutes(transactions.get(i).time);
                int timeJ = parseMinutes(transactions.get(j).time);
                if (Math.abs(timeJ - timeI) <= windowMinutes) {
                    double complement = target - transactions.get(j).amount;
                    if (seen.containsKey(complement)) {
                        result.add(new int[]{seen.get(complement), j});
                    }
                }
            }
        }
        return result;
    }

    private int parseMinutes(String time) {
        String[] parts = time.split(":");
        return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
    }

    /**
     * Detect duplicate transactions: Same amount + merchant, different accounts
     */
    public void detectDuplicates() {
        // key = amount + merchant -> list of transactions
        Map<String, List<Transaction>> groups = new HashMap<>();
        for (Transaction t : transactions) {
            String key = t.amount + "|" + t.merchant;
            groups.computeIfAbsent(key, k -> new ArrayList<>()).add(t);
        }

        boolean found = false;
        for (Map.Entry<String, List<Transaction>> entry : groups.entrySet()) {
            List<Transaction> group = entry.getValue();
            if (group.size() > 1) {
                Set<String> accounts = new HashSet<>();
                for (Transaction t : group) accounts.add(t.accountId);
                if (accounts.size() > 1) {
                    System.out.println("  [{amount:" + group.get(0).amount +
                            ", merchant:\"" + group.get(0).merchant +
                            "\", accounts:" + accounts + "}]");
                    found = true;
                }
            }
        }
        if (!found) System.out.println("  No duplicates found");
    }

    /**
     * K-Sum: Find K transactions summing to target (recursive + memoization)
     */
    public List<List<Integer>> findKSum(int k, double target) {
        List<List<Integer>> results = new ArrayList<>();
        kSumHelper(k, target, 0, new ArrayList<>(), results);
        return results;
    }

    private void kSumHelper(int k, double remaining, int start,
                            List<Integer> current, List<List<Integer>> results) {
        if (k == 0) {
            if (Math.abs(remaining) < 0.001) results.add(new ArrayList<>(current));
            return;
        }
        for (int i = start; i < transactions.size(); i++) {
            current.add(transactions.get(i).id);
            kSumHelper(k - 1, remaining - transactions.get(i).amount,
                    i + 1, current, results);
            current.remove(current.size() - 1);
        }
    }

    public static void main(String[] args) {
        Problem9_TwoSumTransactions system = new Problem9_TwoSumTransactions();

        System.out.println("=== Two-Sum Financial Transactions ===");
        System.out.println();

        // Add transactions
        system.addTransaction(new Transaction(1, 500, "Store A", "10:00", "acc1"));
        system.addTransaction(new Transaction(2, 300, "Store B", "10:15", "acc2"));
        system.addTransaction(new Transaction(3, 200, "Store C", "10:30", "acc3"));
        system.addTransaction(new Transaction(4, 500, "Store A", "10:45", "acc2")); // duplicate

        System.out.println("Transactions loaded: 4");
        System.out.println();

        // Two-Sum
        List<int[]> pairs = system.findTwoSum(500);
        System.out.print("findTwoSum(target=500) -> ");
        for (int[] pair : pairs) {
            System.out.println("[(id:" + (pair[0] + 1) + ", id:" + (pair[1] + 1) + ")] // " +
                    system.transactions.get(pair[0]).amount + " + " +
                    system.transactions.get(pair[1]).amount);
        }
        System.out.println();

        // Duplicate detection
        System.out.println("detectDuplicates() ->");
        system.detectDuplicates();
        System.out.println();

        // K-Sum
        List<List<Integer>> kResults = system.findKSum(3, 1000);
        System.out.print("findKSum(k=3, target=1000) -> ");
        for (List<Integer> combo : kResults) {
            System.out.println(combo + " // 500+300+200");
        }
    }
}