import java.util.*;
import java.util.concurrent.*;

/**
 * ========================================================
 * Problem 2: E-commerce Flash Sale Inventory Manager
 * ========================================================
 * Concepts: HashMap for stock, thread-safe ops, waiting list
 */
public class Problem2_FlashSaleInventory {

    // productId -> stock count (thread-safe)
    private Map<String, Integer> inventory = new HashMap<>();

    // productId -> waiting list of userIds (FIFO using LinkedHashMap)
    private Map<String, Queue<Integer>> waitingLists = new LinkedHashMap<>();

    /** Add a product with initial stock */
    public void addProduct(String productId, int stock) {
        inventory.put(productId, stock);
        waitingLists.put(productId, new LinkedList<>());
    }

    /** Check current stock for a product O(1) */
    public int checkStock(String productId) {
        return inventory.getOrDefault(productId, 0);
    }

    /** Attempt to purchase - thread-safe with synchronized */
    public synchronized String purchaseItem(String productId, int userId) {
        int stock = inventory.getOrDefault(productId, 0);
        if (stock > 0) {
            inventory.put(productId, stock - 1);
            return "Success, " + (stock - 1) + " units remaining";
        } else {
            Queue<Integer> waitList = waitingLists.get(productId);
            waitList.offer(userId);
            return "Added to waiting list, position #" + waitList.size();
        }
    }

    /** Get waiting list size */
    public int getWaitingListSize(String productId) {
        return waitingLists.getOrDefault(productId, new LinkedList<>()).size();
    }

    public static void main(String[] args) {
        Problem2_FlashSaleInventory sale = new Problem2_FlashSaleInventory();
        sale.addProduct("IPHONE15_256GB", 100);

        System.out.println("=== Flash Sale Inventory Manager ===");
        System.out.println();
        System.out.println("checkStock(\"IPHONE15_256GB\") -> " +
                sale.checkStock("IPHONE15_256GB") + " units available");
        System.out.println();

        // First two purchases
        System.out.println("purchaseItem(\"IPHONE15_256GB\", 12345) -> " +
                sale.purchaseItem("IPHONE15_256GB", 12345));
        System.out.println("purchaseItem(\"IPHONE15_256GB\", 67890) -> " +
                sale.purchaseItem("IPHONE15_256GB", 67890));
        System.out.println();

        // Exhaust remaining 98 units
        System.out.println("... purchasing remaining 98 units ...");
        for (int i = 3; i <= 100; i++) {
            sale.purchaseItem("IPHONE15_256GB", i);
        }
        System.out.println();

        // Now stock is 0 - next buyer goes to waiting list
        System.out.println("purchaseItem(\"IPHONE15_256GB\", 99999) -> " +
                sale.purchaseItem("IPHONE15_256GB", 99999));
        System.out.println("purchaseItem(\"IPHONE15_256GB\", 88888) -> " +
                sale.purchaseItem("IPHONE15_256GB", 88888));
    }
}