import java.util.HashMap;
import java.util.Map;

/**
 * ========================================================
 * ABSTRACT CLASS - Room
 * ========================================================
 *
 * Use Case 2: Basic Room Types & Static Availability
 *
 * Description:
 * This abstract class represents a generic hotel room.
 *
 * It models attributes that are intrinsic to a room type
 * and remain constant regardless of availability.
 *
 * Inventory-related concerns are intentionally excluded.
 *
 * @version 2.0
 */
abstract class Room1 {

    /** Number of beds available in the room. */
    protected int numberOfBeds;

    /** Total size of the room in square feet. */
    protected int squareFeet;

    /** Price charged per night for this room type. */
    protected double pricePerNight;

    /**
     * Constructor used by child classes to
     * initialize common room attributes.
     *
     * @param numberOfBeds number of beds in the room
     * @param squareFeet total room size
     * @param pricePerNight cost per night
     */
    public Room1(int numberOfBeds, int squareFeet, double pricePerNight) {
        this.numberOfBeds = numberOfBeds;
        this.squareFeet = squareFeet;
        this.pricePerNight = pricePerNight;
    }

    /** Displays room details. */
    public void displayRoomDetails() {
        System.out.println("Beds: " + numberOfBeds);
        System.out.println("Size: " + squareFeet + " sqft");
        System.out.println("Price per night: " + pricePerNight);
    }
}

/**
 * ========================================================
 * CLASS - SingleRoom
 * ========================================================
 *
 * Represents a single room in the hotel.
 *
 * @version 2.0
 */
class SingleRoom extends Room1 {

    /**
     * Initializes a SingleRoom with
     * predefined attributes.
     */
    public SingleRoom() { super(1, 250, 1500.0); }
}

/**
 * ========================================================
 * CLASS - DoubleRoom
 * ========================================================
 *
 * Represents a double room in the hotel.
 *
 * @version 2.0
 */
class DoubleRoom extends Room1 {

    /**
     * Initializes a DoubleRoom with
     * predefined attributes.
     */
    public DoubleRoom() { super(2, 400, 2500.0); }
}

/**
 * ========================================================
 * CLASS - SuiteRoom
 * ========================================================
 *
 * Represents a suite room in the hotel.
 *
 * @version 2.0
 */
class SuiteRoom extends Room1 {

    /**
     * Initializes a SuiteRoom with
     * predefined attributes.
     */
    public SuiteRoom() { super(3, 750, 5000.0); }
}

/**
 * ========================================================
 * CLASS - RoomInventory
 * ========================================================
 *
 * Use Case 3: Centralized Room Inventory Management
 *
 * Description:
 * This class acts as the single source of truth
 * for room availability in the hotel.
 *
 * Room pricing and characteristics are obtained
 * from Room objects, not duplicated here.
 *
 * This avoids multiple sources of truth and
 * keeps responsibilities clearly separated.
 *
 * @version 3.1
 */
class RoomInventory {

    /**
     * Stores available room count for each room type.
     *
     * Key   -> Room type name
     * Value -> Available room count
     */
    private Map<String, Integer> roomAvailability;

    /**
     * Constructor initializes the inventory
     * with default availability values.
     */
    public RoomInventory() {
        roomAvailability = new HashMap<>();
        initializeInventory();
    }

    /**
     * Initializes room availability data.
     *
     * This method centralizes inventory setup
     * instead of using scattered variables.
     */
    private void initializeInventory() {
        roomAvailability.put("Single", 5);
        roomAvailability.put("Double", 3);
        roomAvailability.put("Suite", 2);
    }

    /**
     * Returns the current availability map.
     *
     * @return map of room type to available count
     */
    public Map<String, Integer> getRoomAvailability() { return roomAvailability; }

    /**
     * Updates availability for a specific room type.
     *
     * @param roomType the room type to update
     * @param count new availability count
     */
    public void updateAvailability(String roomType, int count) { roomAvailability.put(roomType, count); }
}

/**
 * ========================================================
 * MAIN CLASS - UseCase3InventorySetup
 * ========================================================
 *
 * Use Case 3: Centralized Room Inventory Management
 *
 * Description:
 * This class demonstrates how room availability
 * is managed using a centralized inventory.
 *
 * Room objects are used to retrieve pricing
 * and room characteristics.
 *
 * No booking or search logic is introduced here.
 *
 * @version 3.1
 */
public class UseCase3InventorySetup {

    /**
     * Application entry point.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {

        // Create room instances
        SingleRoom singleRoom = new SingleRoom();
        DoubleRoom doubleRoom = new DoubleRoom();
        SuiteRoom suiteRoom = new SuiteRoom();

        // Create centralized inventory
        RoomInventory inventory = new RoomInventory();
        Map<String, Integer> availability = inventory.getRoomAvailability();

        // Display inventory status
        System.out.println("Hotel Room Inventory Status");
        System.out.println();

        System.out.println("Single Room:");
        singleRoom.displayRoomDetails();
        System.out.println("Available Rooms: " + availability.get("Single"));
        System.out.println();

        System.out.println("Double Room:");
        doubleRoom.displayRoomDetails();
        System.out.println("Available Rooms: " + availability.get("Double"));
        System.out.println();

        System.out.println("Suite Room:");
        suiteRoom.displayRoomDetails();
        System.out.println("Available Rooms: " + availability.get("Suite"));
    }
}