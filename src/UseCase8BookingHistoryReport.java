import java.util.ArrayList;
import java.util.List;

/**
 * ========================================================
 * CLASS - Reservation8
 * ========================================================
 *
 * Use Case 8: Booking History & Reporting
 *
 * @version 8.0
 */
class Reservation8 {

    /** Name of the guest making the booking. */
    private String guestName;

    /** Requested room type. */
    private String roomType;

    /** Assigned room ID. */
    private String roomId;

    public Reservation8(String guestName, String roomType, String roomId) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    /** @return guest name */
    public String getGuestName() { return guestName; }

    /** @return requested room type */
    public String getRoomType() { return roomType; }

    /** @return assigned room ID */
    public String getRoomId() { return roomId; }
}

/**
 * ========================================================
 * CLASS - BookingHistory
 * ========================================================
 *
 * Use Case 8: Booking History & Reporting
 *
 * Description:
 * This class maintains a record of
 * confirmed reservations.
 *
 * It provides ordered storage for
 * historical and reporting purposes.
 *
 * @version 8.0
 */
class BookingHistory {

    /** List that stores confirmed reservations. */
    private List<Reservation8> confirmedReservations;

    /** Initializes an empty booking history. */
    public BookingHistory() { confirmedReservations = new ArrayList<>(); }

    /**
     * Adds a confirmed reservation
     * to booking history.
     *
     * @param reservation confirmed booking
     */
    public void addReservation(Reservation8 reservation) { confirmedReservations.add(reservation); }

    /**
     * Returns all confirmed reservations.
     *
     * @return list of reservations
     */
    public List<Reservation8> getConfirmedReservations() { return confirmedReservations; }
}

/**
 * ========================================================
 * CLASS - BookingReportService
 * ========================================================
 *
 * Use Case 8: Booking History & Reporting
 *
 * Description:
 * This class generates reports
 * from booking history data.
 *
 * Reporting logic is separated
 * from data storage.
 *
 * @version 8.0
 */
class BookingReportService {

    /**
     * Displays a summary report
     * of all confirmed bookings.
     *
     * @param history booking history
     */
    public void generateReport(BookingHistory history) {
        List<Reservation8> reservations = history.getConfirmedReservations();

        System.out.println("Booking History Report");
        System.out.println("Total Bookings: " + reservations.size());
        System.out.println();

        for (Reservation8 reservation : reservations) {
            System.out.println("Guest: " + reservation.getGuestName());
            System.out.println("Room Type: " + reservation.getRoomType());
            System.out.println("Room ID: " + reservation.getRoomId());
            System.out.println();
        }
    }
}

/**
 * ========================================================
 * MAIN CLASS - UseCase8BookingHistory
 * ========================================================
 *
 * Use Case 8: Booking History & Reporting
 *
 * Description:
 * This class demonstrates how confirmed
 * bookings are recorded and reported.
 *
 * Booking history is maintained separately
 * from inventory and allocation logic.
 *
 * @version 8.0
 */
public class UseCase8BookingHistoryReport {

    /**
     * Application entry point.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {

        // Create booking history
        BookingHistory history = new BookingHistory();

        // Add confirmed reservations
        history.addReservation(new Reservation8("Abhi", "Single", "Single-1"));
        history.addReservation(new Reservation8("Subha", "Single", "Single-2"));
        history.addReservation(new Reservation8("Vanmathi", "Suite", "Suite-1"));

        // Generate report
        BookingReportService reportService = new BookingReportService();
        reportService.generateReport(history);
    }
}