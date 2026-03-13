import java.util.HashMap;
import java.util.Map;

/**
 * ========================================================
 * ABSTRACT CLASS - Room
 * ========================================================
 *
 * Use Case 4: Room Search & Availability Check
 *
 * Description:
 * This abstract class represents a generic hotel room.
 *
 * @version 4.0
 */
abstract class Room4 {

    protected int numberOfBeds;
    protected int squareFeet;
    protected double pricePerNight;

    public Room4(int numberOfBeds, int squareFeet, double pricePerNight) {
        this.numberOfBeds = numberOfBeds;
        this.squareFeet = squareFeet;
        this.pricePerNight = pricePerNight;
    }

    public void displayRoomDetails() {
        System.out.println("Beds: " + numberOfBeds);
        System.out.println("Size: " + squareFeet + " sqft");
        System.out.println("Price per night: " + pricePerNight);
    }
}

class SingleRoom4 extends Room4 {
    public SingleRoom4() { super(1, 250, 1500.0); }
}

class DoubleRoom4 extends Room4 {
    public DoubleRoom4() { super(2, 400, 2500.0); }
}

class SuiteRoom4 extends Room4 {
    public SuiteRoom4() { super(3, 750, 5000.0); }
}

/**
 * ========================================================
 * CLASS - RoomInventory4
 * ========================================================
 *
 * Use Case 4: Room Search & Availability Check
 *
 * @version 4.0
 */
class RoomInventory4 {

    private Map<String, Integer> roomAvailability;

    public RoomInventory4() {
        roomAvailability = new HashMap<>();
        initializeInventory();
    }

    private void initializeInventory() {
        roomAvailability.put("Single", 5);
        roomAvailability.put("Double", 3);
        roomAvailability.put("Suite", 2);
    }

    public Map<String, Integer> getRoomAvailability() { return roomAvailability; }

    public void updateAvailability(String roomType, int count) { roomAvailability.put(roomType, count); }
}

/**
 * ========================================================
 * CLASS - RoomSearchService
 * ========================================================
 *
 * Use Case 4: Room Search & Availability Check
 *
 * Description:
 * This class provides search functionality
 * for guests to view available rooms.
 *
 * It reads room availability from inventory
 * and room details from Room objects.
 *
 * No inventory mutation or booking logic
 * is performed in this class.
 *
 * @version 4.0
 */
class RoomSearchService {

    /**
     * Displays available rooms along with
     * their details and pricing.
     *
     * This method performs read-only access
     * to inventory and room data.
     *
     * @param inventory  centralized room inventory
     * @param singleRoom single room definition
     * @param doubleRoom double room definition
     * @param suiteRoom  suite room definition
     */
    public void searchAvailableRooms(
            RoomInventory4 inventory,
            Room4 singleRoom,
            Room4 doubleRoom,
            Room4 suiteRoom) {

        Map<String, Integer> availability = inventory.getRoomAvailability();

        // Check and display Single Room availability
        if (availability.get("Single") > 0) {
            System.out.println("Single Room:");
            singleRoom.displayRoomDetails();
            System.out.println("Available: " + availability.get("Single"));
            System.out.println();
        }

        // Check and display Double Room availability
        if (availability.get("Double") > 0) {
            System.out.println("Double Room:");
            doubleRoom.displayRoomDetails();
            System.out.println("Available: " + availability.get("Double"));
            System.out.println();
        }

        // Check and display Suite Room availability
        if (availability.get("Suite") > 0) {
            System.out.println("Suite Room:");
            suiteRoom.displayRoomDetails();
            System.out.println("Available: " + availability.get("Suite"));
        }
    }
}

/**
 * ========================================================
 * MAIN CLASS - UseCase4RoomSearch
 * ========================================================
 *
 * Use Case 4: Room Search & Availability Check
 *
 * Description:
 * This class demonstrates how guests
 * can view available rooms without
 * modifying inventory data.
 *
 * The system enforces read-only access
 * by design and usage discipline.
 *
 * @version 4.0
 */
public class UseCase4RoomSearch {

    /**
     * Application entry point.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {

        // Create room instances
        SingleRoom4 singleRoom = new SingleRoom4();
        DoubleRoom4 doubleRoom = new DoubleRoom4();
        SuiteRoom4 suiteRoom = new SuiteRoom4();

        // Create centralized inventory
        RoomInventory4 inventory = new RoomInventory4();

        // Create search service
        RoomSearchService searchService = new RoomSearchService();

        System.out.println("Room Search");
        System.out.println();

        // Search and display available rooms
        searchService.searchAvailableRooms(inventory, singleRoom, doubleRoom, suiteRoom);
    }
}