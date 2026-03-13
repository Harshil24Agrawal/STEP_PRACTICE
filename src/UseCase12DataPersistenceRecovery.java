import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * ========================================================
 * CLASS - RoomInventory12
 * ========================================================
 *
 * Use Case 12: Data Persistence & System Recovery
 *
 * @version 12.0
 */
class RoomInventory12 {

    private Map<String, Integer> roomAvailability;

    public RoomInventory12() {
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
 * CLASS - FilePersistenceService
 * ========================================================
 *
 * Use Case 12: Data Persistence & System Recovery
 *
 * Description:
 * This class is responsible for persisting
 * critical system state to a plain text file.
 *
 * It supports:
 * - Saving room inventory state
 * - Restoring inventory on system startup
 *
 * No database or serialization framework
 * is used in this use case.
 *
 * @version 12.0
 */
class FilePersistenceService {

    /**
     * Saves room inventory state to a file.
     *
     * Each line follows the format:
     * roomType=availableCount
     *
     * @param inventory centralized room inventory
     * @param filePath  path to persistence file
     */
    public void saveInventory(RoomInventory12 inventory, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Map.Entry<String, Integer> entry : inventory.getRoomAvailability().entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue());
                writer.newLine();
            }
            System.out.println("Inventory saved to: " + filePath);
        } catch (IOException e) {
            System.out.println("Error saving inventory: " + e.getMessage());
        }
    }

    /**
     * Loads room inventory state from a file.
     *
     * @param inventory centralized room inventory
     * @param filePath  path to persistence file
     */
    public void loadInventory(RoomInventory12 inventory, String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    String roomType = parts[0].trim();
                    int count = Integer.parseInt(parts[1].trim());
                    inventory.updateAvailability(roomType, count);
                }
            }
            System.out.println("Inventory loaded from: " + filePath);
        } catch (IOException e) {
            System.out.println("Error loading inventory: " + e.getMessage());
        }
    }
}

/**
 * ========================================================
 * MAIN CLASS - UseCase12DataPersistence
 * ========================================================
 *
 * Use Case 12: Data Persistence & System Recovery
 *
 * Description:
 * This class demonstrates how room inventory
 * state is saved and restored from a file.
 *
 * This simulates system shutdown and recovery
 * without using a database or framework.
 *
 * @version 12.0
 */
public class UseCase12DataPersistenceRecovery {

    /**
     * Application entry point.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {

        String filePath = "inventory.txt";

        // --- Simulate system shutdown: save state ---
        RoomInventory12 inventory = new RoomInventory12();

        // Simulate some bookings
        inventory.updateAvailability("Single", 3);
        inventory.updateAvailability("Double", 1);
        inventory.updateAvailability("Suite", 2);

        FilePersistenceService persistenceService = new FilePersistenceService();

        System.out.println("Data Persistence & System Recovery");
        System.out.println();

        System.out.println("Saving inventory state...");
        persistenceService.saveInventory(inventory, filePath);
        System.out.println();

        // --- Simulate system restart: restore state ---
        RoomInventory12 restoredInventory = new RoomInventory12();

        System.out.println("Restoring inventory state...");
        persistenceService.loadInventory(restoredInventory, filePath);
        System.out.println();

        // Display restored inventory
        System.out.println("Restored Inventory:");
        for (Map.Entry<String, Integer> entry : restoredInventory.getRoomAvailability().entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " available");
        }
    }
}