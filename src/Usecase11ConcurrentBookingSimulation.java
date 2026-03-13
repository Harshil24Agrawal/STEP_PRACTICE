import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * ========================================================
 * CLASS - Reservation11
 * ========================================================
 *
 * Use Case 11: Concurrent Booking Simulation
 *
 * @version 11.0
 */
class Reservation11 {

    private String guestName;
    private String roomType;

    public Reservation11(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
}

/**
 * ========================================================
 * CLASS - BookingRequestQueue11
 * ========================================================
 *
 * Use Case 11: Concurrent Booking Simulation
 *
 * @version 11.0
 */
class BookingRequestQueue11 {

    private Queue<Reservation11> requestQueue;

    public BookingRequestQueue11() { requestQueue = new LinkedList<>(); }

    public void addRequest(Reservation11 reservation) { requestQueue.offer(reservation); }

    public Reservation11 getNextRequest() { return requestQueue.poll(); }

    public boolean hasPendingRequests() { return !requestQueue.isEmpty(); }
}

/**
 * ========================================================
 * CLASS - RoomInventory11
 * ========================================================
 *
 * Use Case 11: Concurrent Booking Simulation
 *
 * @version 11.0
 */
class RoomInventory11 {

    private Map<String, Integer> roomAvailability;

    public RoomInventory11() {
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
 * CLASS - RoomAllocationService11
 * ========================================================
 *
 * Use Case 11: Concurrent Booking Simulation
 *
 * @version 11.0
 */
class RoomAllocationService11 {

    private Set<String> allocatedRoomIds;
    private Map<String, Set<String>> assignedRoomsByType;

    public RoomAllocationService11() {
        allocatedRoomIds = new HashSet<>();
        assignedRoomsByType = new HashMap<>();
    }

    public synchronized void allocateRoom(Reservation11 reservation, RoomInventory11 inventory) {
        String roomType = reservation.getRoomType();
        Map<String, Integer> availability = inventory.getRoomAvailability();

        if (availability.getOrDefault(roomType, 0) > 0) {
            String roomId = generateRoomId(roomType);
            allocatedRoomIds.add(roomId);
            assignedRoomsByType.computeIfAbsent(roomType, k -> new HashSet<>()).add(roomId);
            inventory.updateAvailability(roomType, availability.get(roomType) - 1);
            System.out.println("[" + Thread.currentThread().getName() + "] "
                    + "Booking confirmed for Guest: " + reservation.getGuestName()
                    + ", Room ID: " + roomId);
        } else {
            System.out.println("[" + Thread.currentThread().getName() + "] "
                    + "No available rooms for Guest: " + reservation.getGuestName()
                    + " (Room Type: " + roomType + ")");
        }
    }

    private String generateRoomId(String roomType) {
        int count = assignedRoomsByType.getOrDefault(roomType, new HashSet<>()).size() + 1;
        return roomType + "-" + count;
    }
}

/**
 * ========================================================
 * CLASS - ConcurrentBookingProcessor
 * ========================================================
 *
 * Use Case 11: Concurrent Booking Simulation
 *
 * Description:
 * This class represents a booking processor
 * that can be executed by multiple threads.
 *
 * It demonstrates how shared resources
 * such as booking queues and inventory
 * must be accessed in a thread-safe manner.
 *
 * @version 11.0
 */
class ConcurrentBookingProcessor implements Runnable {

    private BookingRequestQueue11 bookingQueue;
    private RoomInventory11 inventory;
    private RoomAllocationService11 allocationService;

    public ConcurrentBookingProcessor(
            BookingRequestQueue11 bookingQueue,
            RoomInventory11 inventory,
            RoomAllocationService11 allocationService) {
        this.bookingQueue = bookingQueue;
        this.inventory = inventory;
        this.allocationService = allocationService;
    }

    @Override
    public void run() {
        while (true) {
            Reservation11 reservation;

            /*
             * Synchronize on the booking queue to ensure
             * that only one thread can retrieve a request
             * at a time.
             */
            synchronized (bookingQueue) {
                if (!bookingQueue.hasPendingRequests()) {
                    break;
                }
                reservation = bookingQueue.getNextRequest();
            }

            /*
             * Allocation also mutates shared inventory.
             * Synchronization ensures atomic allocation.
             */
            synchronized (inventory) {
                allocationService.allocateRoom(reservation, inventory);
            }
        }
    }
}

/**
 * ========================================================
 * MAIN CLASS - UseCase11ConcurrentBooking
 * ========================================================
 *
 * Use Case 11: Concurrent Booking Simulation
 *
 * Description:
 * This class demonstrates how multiple threads
 * can process booking requests concurrently
 * in a thread-safe manner.
 *
 * Shared resources are protected using
 * synchronized blocks to prevent race conditions.
 *
 * @version 11.0
 */
public class Usecase11ConcurrentBookingSimulation {

    /**
     * Application entry point.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) throws InterruptedException {

        // Create shared booking queue
        BookingRequestQueue11 bookingQueue = new BookingRequestQueue11();
        bookingQueue.addRequest(new Reservation11("Alice", "Single"));
        bookingQueue.addRequest(new Reservation11("Bob", "Double"));
        bookingQueue.addRequest(new Reservation11("Charlie", "Suite"));
        bookingQueue.addRequest(new Reservation11("Diana", "Single"));
        bookingQueue.addRequest(new Reservation11("Eve", "Double"));

        // Create shared inventory and allocation service
        RoomInventory11 inventory = new RoomInventory11();
        RoomAllocationService11 allocationService = new RoomAllocationService11();

        System.out.println("Concurrent Booking Simulation");
        System.out.println();

        // Create and start multiple threads
        Thread thread1 = new Thread(
                new ConcurrentBookingProcessor(bookingQueue, inventory, allocationService), "Thread-1");
        Thread thread2 = new Thread(
                new ConcurrentBookingProcessor(bookingQueue, inventory, allocationService), "Thread-2");

        thread1.start();
        thread2.start();

        // Wait for both threads to finish
        thread1.join();
        thread2.join();

        System.out.println();
        System.out.println("All booking requests processed.");
    }
}