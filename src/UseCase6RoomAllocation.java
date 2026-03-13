import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * ========================================================
 * CLASS - Reservation6
 * ========================================================
 *
 * Use Case 6: Reservation Confirmation & Room Allocation
 *
 * @version 6.0
 */
class Reservation6 {

    /** Name of the guest making the booking. */
    private String guestName;

    /** Requested room type. */
    private String roomType;

    public Reservation6(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    /** @return guest name */
    public String getGuestName() { return guestName; }

    /** @return requested room type */
    public String getRoomType() { return roomType; }
}

/**
 * ========================================================
 * CLASS - BookingRequestQueue6
 * ========================================================
 *
 * Use Case 6: Reservation Confirmation & Room Allocation
 *
 * @version 6.0
 */
class BookingRequestQueue6 {

    /** Queue that stores booking requests. */
    private Queue<Reservation6> requestQueue;

    /** Initializes an empty booking queue. */
    public BookingRequestQueue6() { requestQueue = new LinkedList<>(); }

    /**
     * Adds a booking request to the queue.
     *
     * @param reservation booking request
     */
    public void addRequest(Reservation6 reservation) { requestQueue.offer(reservation); }

    /**
     * Retrieves and removes the next booking request from the queue.
     *
     * @return next reservation request
     */
    public Reservation6 getNextRequest() { return requestQueue.poll(); }

    /**
     * Checks whether there are pending booking requests.
     *
     * @return true if queue is not empty
     */
    public boolean hasPendingRequests() { return !requestQueue.isEmpty(); }
}

/**
 * ========================================================
 * CLASS - RoomInventory6
 * ========================================================
 *
 * Use Case 6: Reservation Confirmation & Room Allocation
 *
 * @version 6.0
 */
class RoomInventory6 {

    private Map<String, Integer> roomAvailability;

    public RoomInventory6() {
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
 * CLASS - RoomAllocationService
 * ========================================================
 *
 * Use Case 6: Reservation Confirmation & Room Allocation
 *
 * Description:
 * This class is responsible for confirming
 * booking requests and assigning rooms.
 *
 * It ensures:
 * - Each room ID is unique
 * - Inventory is updated immediately
 * - No room is double-booked
 *
 * @version 6.0
 */
class RoomAllocationService {

    /**
     * Stores all allocated room IDs to
     * prevent duplicate assignments.
     */
    private Set<String> allocatedRoomIds;

    /**
     * Stores assigned room IDs by room type.
     *
     * Key   -> Room type
     * Value -> Set of assigned room IDs
     */
    private Map<String, Set<String>> assignedRoomsByType;

    /** Initializes allocation tracking structures. */
    public RoomAllocationService() {
        allocatedRoomIds = new HashSet<>();
        assignedRoomsByType = new HashMap<>();
    }

    /**
     * Confirms a booking request by assigning
     * a unique room ID and updating inventory.
     *
     * @param reservation booking request
     * @param inventory   centralized room inventory
     */
    public void allocateRoom(Reservation6 reservation, RoomInventory6 inventory) {
        String roomType = reservation.getRoomType();
        Map<String, Integer> availability = inventory.getRoomAvailability();

        if (availability.getOrDefault(roomType, 0) > 0) {
            String roomId = generateRoomId(roomType);
            allocatedRoomIds.add(roomId);
            assignedRoomsByType.computeIfAbsent(roomType, k -> new HashSet<>()).add(roomId);
            inventory.updateAvailability(roomType, availability.get(roomType) - 1);
            System.out.println("Booking confirmed for Guest: " + reservation.getGuestName()
                    + ", Room ID: " + roomId);
        } else {
            System.out.println("No available rooms for Guest: " + reservation.getGuestName()
                    + " (Room Type: " + roomType + ")");
        }
    }

    /**
     * Generates a unique room ID
     * for the given room type.
     *
     * @param roomType type of room
     * @return unique room ID
     */
    private String generateRoomId(String roomType) {
        int count = assignedRoomsByType.getOrDefault(roomType, new HashSet<>()).size() + 1;
        return roomType + "-" + count;
    }
}

/**
 * ========================================================
 * MAIN CLASS - UseCase6RoomAllocation
 * ========================================================
 *
 * Use Case 6: Reservation Confirmation & Room Allocation
 *
 * Description:
 * This class demonstrates how booking
 * requests are confirmed and rooms
 * are allocated safely.
 *
 * It consumes booking requests in FIFO
 * order and updates inventory immediately.
 *
 * @version 6.0
 */
public class UseCase6RoomAllocation {

    /**
     * Application entry point.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {

        // Create booking queue and add requests
        BookingRequestQueue6 bookingQueue = new BookingRequestQueue6();
        bookingQueue.addRequest(new Reservation6("Abhi", "Single"));
        bookingQueue.addRequest(new Reservation6("Subha", "Single"));
        bookingQueue.addRequest(new Reservation6("Vanmathi", "Suite"));

        // Create inventory and allocation service
        RoomInventory6 inventory = new RoomInventory6();
        RoomAllocationService allocationService = new RoomAllocationService();

        System.out.println("Room Allocation Processing");

        // Process requests in FIFO order
        while (bookingQueue.hasPendingRequests()) {
            Reservation6 next = bookingQueue.getNextRequest();
            allocationService.allocateRoom(next, inventory);
        }
    }
}