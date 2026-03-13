import java.util.*;

/**
 * ========================================================
 * Problem 8: Parking Lot Management with Open Addressing
 * ========================================================
 * Concepts: Open addressing, linear probing, custom hash, load factor
 */
public class Problem8_ParkingLot {

    enum SpotStatus { EMPTY, OCCUPIED, DELETED }

    static class ParkingSpot {
        SpotStatus status = SpotStatus.EMPTY;
        String licensePlate;
        long entryTime;
        int probesUsed;
    }

    private static final int SIZE = 500;
    private static final double RATE_PER_HOUR = 5.0; // $5 per hour

    private ParkingSpot[] spots = new ParkingSpot[SIZE];
    private int occupiedCount = 0;
    private int totalProbes = 0;
    private int totalVehicles = 0;
    private Map<Integer, Integer> peakHourCounts = new HashMap<>();

    public Problem8_ParkingLot() {
        for (int i = 0; i < SIZE; i++) spots[i] = new ParkingSpot();
    }

    /** Hash function: license plate -> preferred spot number */
    private int hash(String licensePlate) {
        int hash = 0;
        for (char c : licensePlate.toCharArray()) {
            hash = (hash * 31 + c) % SIZE;
        }
        return Math.abs(hash);
    }

    /** Park a vehicle using linear probing */
    public String parkVehicle(String licensePlate) {
        int preferred = hash(licensePlate);
        int probes = 0;
        int idx = preferred;

        StringBuilder probeLog = new StringBuilder();
        probeLog.append("Assigned spot #").append(preferred);

        while (probes < SIZE) {
            if (spots[idx].status != SpotStatus.OCCUPIED) {
                spots[idx].status = SpotStatus.OCCUPIED;
                spots[idx].licensePlate = licensePlate;
                spots[idx].entryTime = System.currentTimeMillis();
                spots[idx].probesUsed = probes;
                occupiedCount++;
                totalProbes += probes;
                totalVehicles++;

                // Track peak hour
                int hour = new java.util.Date().getHours();
                peakHourCounts.merge(hour, 1, Integer::sum);

                if (probes == 0) {
                    probeLog.append(" (0 probes)");
                } else {
                    probeLog.append("... Spot #").append(idx).append(" (").append(probes).append(" probe").append(probes > 1 ? "s)" : ")");
                }
                return probeLog.toString();
            }
            probeLog.append("... occupied");
            idx = (idx + 1) % SIZE;
            probes++;
        }
        return "Parking lot is FULL";
    }

    /** Vehicle exits - frees spot and computes fee */
    public String exitVehicle(String licensePlate) {
        for (int i = 0; i < SIZE; i++) {
            if (spots[i].status == SpotStatus.OCCUPIED &&
                    licensePlate.equals(spots[i].licensePlate)) {
                long durationMs = System.currentTimeMillis() - spots[i].entryTime;
                double hours = Math.max(durationMs / 3600000.0, 0.25); // min 15 min charge
                double fee = Math.round(hours * RATE_PER_HOUR * 100.0) / 100.0;
                long minutes = (durationMs / 60000) % 60;
                long hrs = durationMs / 3600000;

                spots[i].status = SpotStatus.DELETED;
                spots[i].licensePlate = null;
                occupiedCount--;

                return String.format("Spot #%d freed, Duration: %dh %dm, Fee: $%.2f",
                        i, hrs, minutes, fee);
            }
        }
        return "Vehicle not found: " + licensePlate;
    }

    /** Parking statistics */
    public void getStatistics() {
        double occupancy = occupiedCount * 100.0 / SIZE;
        double avgProbes = totalVehicles > 0 ? totalProbes * 1.0 / totalVehicles : 0;
        int peakHour = peakHourCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse(-1);

        System.out.printf("Occupancy: %.0f%%, Avg Probes: %.1f, Peak Hour: %d-%d%n",
                occupancy, avgProbes, peakHour, peakHour + 1);
    }

    public static void main(String[] args) throws InterruptedException {
        Problem8_ParkingLot lot = new Problem8_ParkingLot();

        System.out.println("=== Parking Lot with Open Addressing ===");
        System.out.println();

        // Park vehicles that hash to the same spot to show probing
        System.out.println("parkVehicle(\"ABC-1234\") -> " + lot.parkVehicle("ABC-1234"));
        System.out.println("parkVehicle(\"ABC-1235\") -> " + lot.parkVehicle("ABC-1235"));
        System.out.println("parkVehicle(\"XYZ-9999\") -> " + lot.parkVehicle("XYZ-9999"));
        System.out.println();

        // Park more vehicles to show occupancy
        String[] plates = {"DEF-001", "GHI-002", "JKL-003", "MNO-004", "PQR-005",
                "STU-006", "VWX-007", "YZA-008", "BCD-009", "EFG-010"};
        for (String plate : plates) lot.parkVehicle(plate);

        // Wait a moment then exit
        Thread.sleep(100);
        System.out.println("exitVehicle(\"ABC-1234\") -> " + lot.exitVehicle("ABC-1234"));
        System.out.println();

        System.out.print("getStatistics() -> ");
        lot.getStatistics();
    }
}