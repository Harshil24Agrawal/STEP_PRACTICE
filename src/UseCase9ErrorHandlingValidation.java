import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

/**
 * ========================================================
 * CLASS - InvalidBookingException
 * ========================================================
 *
 * Use Case 9: Error Handling & Validation
 *
 * Description:
 * This custom exception represents
 * invalid booking scenarios in the system.
 *
 * Using a domain-specific exception
 * makes error handling clearer and safer.
 *
 * @version 9.0
 */
class InvalidBookingException extends Exception {

    /**
     * Creates an exception with
     * a descriptive error message.
     *
     * @param message error description
     */
    public InvalidBookingException(String message) { super(message); }
}

/**
 * ========================================================
 * CLASS - RoomInventory9
 * ========================================================
 *
 * Use Case 9: Error Handling & Validation
 *
 * @version 9.0
 */
class RoomInventory9 {

    private Map<String, Integer> roomAvailability;

    public RoomInventory9() {
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
 * CLASS - ReservationValidator
 * ========================================================
 *
 * Use Case 9: Error Handling & Validation
 *
 * Description:
 * This class is responsible for validating
 * booking requests before they are processed.
 *
 * All validation rules are centralized
 * to avoid duplication and inconsistency.
 *
 * @version 9.0
 */
class ReservationValidator {

    /**
     * Validates booking input provided by the user.
     *
     * @param guestName name of the guest
     * @param roomType  requested room type
     * @param inventory centralized inventory
     * @throws InvalidBookingException if validation fails
     */
    public void validate(
            String guestName,
            String roomType,
            RoomInventory9 inventory
    ) throws InvalidBookingException {

        // Validate guest name
        if (guestName == null || guestName.trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        // Validate room type (must be exact case: Single, Double, Suite)
        Map<String, Integer> availability = inventory.getRoomAvailability();
        if (!availability.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid room type selected.");
        }

        // Validate availability
        if (availability.get(roomType) <= 0) {
            throw new InvalidBookingException("No available rooms for type: " + roomType);
        }
    }
}

/**
 * ========================================================
 * CLASS - Reservation9
 * ========================================================
 *
 * Use Case 9: Error Handling & Validation
 *
 * @version 9.0
 */
class Reservation9 {

    private String guestName;
    private String roomType;

    public Reservation9(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
}

/**
 * ========================================================
 * CLASS - BookingRequestQueue9
 * ========================================================
 *
 * Use Case 9: Error Handling & Validation
 *
 * @version 9.0
 */
class BookingRequestQueue9 {

    private Queue<Reservation9> requestQueue;

    public BookingRequestQueue9() { requestQueue = new LinkedList<>(); }

    public void addRequest(Reservation9 reservation) { requestQueue.offer(reservation); }

    public Reservation9 getNextRequest() { return requestQueue.poll(); }

    public boolean hasPendingRequests() { return !requestQueue.isEmpty(); }
}

/**
 * ========================================================
 * MAIN CLASS - UseCase9ErrorHandlingValidation
 * ========================================================
 *
 * Use Case 9: Error Handling & Validation
 *
 * Description:
 * This class demonstrates how user input
 * is validated before booking is processed.
 *
 * The system:
 * - Accepts user input
 * - Validates input centrally
 * - Handles errors gracefully
 *
 * @version 9.0
 */
public class UseCase9ErrorHandlingValidation {

    /**
     * Application entry point.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {

        // Display application header
        System.out.println("Booking Validation");

        Scanner scanner = new Scanner(System.in);

        // Initialize required components
        RoomInventory9 inventory = new RoomInventory9();
        ReservationValidator validator = new ReservationValidator();
        BookingRequestQueue9 bookingQueue = new BookingRequestQueue9();

        try {
            System.out.print("Enter guest name: ");
            String guestName = scanner.nextLine();

            System.out.print("Enter room type (Single/Double/Suite): ");
            String roomType = scanner.nextLine();

            // Validate input before processing
            validator.validate(guestName, roomType, inventory);

            // Add to queue if valid
            bookingQueue.addRequest(new Reservation9(guestName, roomType));
            System.out.println("Booking request added for: " + guestName + " (" + roomType + ")");

        } catch (InvalidBookingException e) {
            // Handle domain-specific validation errors
            System.out.println("Booking failed: " + e.getMessage());

        } finally {
            scanner.close();
        }
    }
}