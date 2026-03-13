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
abstract class Room {

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
    public Room(int numberOfBeds, int squareFeet, double pricePerNight) {
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
class SingleRoom1 extends Room1 {

    /**
     * Initializes a SingleRoom with
     * predefined attributes.
     */
    public SingleRoom1() { super(1, 250, 1500.0); }
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
class DoubleRoom1 extends Room1{

    /**
     * Initializes a DoubleRoom with
     * predefined attributes.
     */
    public DoubleRoom1() { super(2, 400, 2500.0); }
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
class SuiteRoom1 extends Room1 {

    /**
     * Initializes a SuiteRoom with
     * predefined attributes.
     */
    public SuiteRoom1() { super(3, 750, 5000.0); }
}

/**
 * ========================================================
 * MAIN CLASS - UseCase2RoomInitialization
 * ========================================================
 *
 * Use Case 2: Basic Room Types & Static Availability
 *
 * Description:
 * This class demonstrates room initialization
 * using domain models before introducing
 * centralized inventory management.
 *
 * Availability is represented using
 * simple variables to highlight limitations.
 *
 * @version 2.0
 */
public class UseCase2RoomInitialization {

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

        // Simple availability variables
        int singleAvailable = 5;
        int doubleAvailable = 3;
        int suiteAvailable = 2;

        // Display room details
        System.out.println("Hotel Room Initialization");
        System.out.println();

        System.out.println("Single Room:");
        singleRoom.displayRoomDetails();
        System.out.println("Available: " + singleAvailable);
        System.out.println();

        System.out.println("Double Room:");
        doubleRoom.displayRoomDetails();
        System.out.println("Available: " + doubleAvailable);
        System.out.println();

        System.out.println("Suite Room:");
        suiteRoom.displayRoomDetails();
        System.out.println("Available: " + suiteAvailable);
    }
}