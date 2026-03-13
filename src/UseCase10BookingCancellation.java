import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * ========================================================
 * CLASS - RoomInventory10
 * ========================================================
 *
 * Use Case 10: Booking Cancellation & Inventory Rollback
 *
 * @version 10.0
 */
class RoomInventory10 {

    private Map<String, Integer> roomAvailability;

    public RoomInventory10() {
        roomAvailability = new HashMap<>();
        roomAvailability.put("Single", 5);
        roomAvailability.put("Double", 3);
        roomAvailability.put("Suite", 2);
    }

    public Map<String, Integer> getRoomAvailability() { return roomAvailability; }

    public void updateAvailability(String roomType, int count) { roomAvailability.put(roomType, count); }
}

/**
 * ========================================================
 * CLASS - CancellationService
 * ========================================================
 *
 * Use Case 10: Booking Cancellation & Inventory Rollback
 *
 * Description:
 * This class is responsible for handling
 * booking cancellations.
 *
 * It ensures that:
 * - Cancelled room IDs are tracked
 * - Inventory is restored correctly
 * - Invalid cancellations are prevented
 *
 * A stack is used to model rollback behavior.
 *
 * @version 10.0
 */
class CancellationService {

    /**
     * Stack that stores cancelled room IDs
     * for rollback tracking (LIFO order).
     */
    private Stack<String> cancellationStack;

    /** Initializes the cancellation service. */
    public CancellationService() {
        cancellationStack = new Stack<>();
    }

    /**
     * Cancels a booking by room ID and restores inventory.
     *
     * @param roomId    the room ID to cancel
     * @param roomType  the room type
     * @param inventory centralized room inventory
     */
    public void cancelBooking(String roomId, String roomType, RoomInventory10 inventory) {
        Map<String, Integer> availability = inventory.getRoomAvailability();

        if (!availability.containsKey(roomType)) {
            System.out.println("Cancellation failed: Invalid room type - " + roomType);
            return;
        }

        // Restore inventory
        int current = availability.get(roomType);
        inventory.updateAvailability(roomType, current + 1);

        // Track cancellation in stack
        cancellationStack.push(roomId);

        System.out.println("Booking cancelled: Room ID - " + roomId);
        System.out.println("Inventory restored for: " + roomType
                + " | Available: " + inventory.getRoomAvailability().get(roomType));
    }

    /**
     * Returns the last cancelled room ID (rollback peek).
     *
     * @return last cancelled room ID or null if empty
     */
    public String getLastCancelled() {
        return cancellationStack.isEmpty() ? null : cancellationStack.peek();
    }

    /**
     * Returns all cancellation records.
     *
     * @return stack of cancelled room IDs
     */
    public Stack<String> getCancellationHistory() { return cancellationStack; }
}

/**
 * ========================================================
 * MAIN CLASS - UseCase10BookingCancellation
 * ========================================================
 *
 * Use Case 10: Booking Cancellation & Inventory Rollback
 *
 * Description:
 * This class demonstrates how confirmed
 * bookings can be cancelled safely.
 *
 * Inventory is restored and rollback
 * history is maintained.
 *
 * @version 10.0
 */
public class UseCase10BookingCancellation {

    /**
     * Application entry point.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {

        // Initialize inventory and cancellation service
        RoomInventory10 inventory = new RoomInventory10();
        CancellationService cancellationService = new CancellationService();

        System.out.println("Booking Cancellation");
        System.out.println();

        // Cancel confirmed bookings
        cancellationService.cancelBooking("Single-1", "Single", inventory);
        System.out.println();

        cancellationService.cancelBooking("Suite-1", "Suite", inventory);
        System.out.println();

        // Display rollback history
        System.out.println("Cancellation History (Last-In-First-Out):");
        Stack<String> history = cancellationService.getCancellationHistory();
        while (!history.isEmpty()) {
            System.out.println("Cancelled Room ID: " + history.pop());
        }
    }
}