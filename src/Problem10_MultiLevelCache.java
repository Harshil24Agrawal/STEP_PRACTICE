import java.util.*;

/**
 * ========================================================
 * Problem 10: Multi-Level Cache System with Hash Tables
 * ========================================================
 * Concepts: Multi-tier LinkedHashMap (LRU), promotion, cache stats
 */
public class Problem10_MultiLevelCache {

    static class VideoData {
        String videoId;
        String title;
        long sizeBytes;

        VideoData(String videoId, String title) {
            this.videoId = videoId;
            this.title = title;
            this.sizeBytes = 1024 * 1024 * 500L; // 500MB per video
        }
    }

    // --- L1: In-memory LRU LinkedHashMap (10,000 capacity) ---
    private final int L1_CAPACITY = 10_000;
    private final LinkedHashMap<String, VideoData> l1Cache;

    // --- L2: SSD-backed simulation (100,000 capacity) ---
    private final int L2_CAPACITY = 100_000;
    private final LinkedHashMap<String, VideoData> l2Cache;

    // --- L3: Database simulation (unlimited) ---
    private final Map<String, VideoData> l3Database = new HashMap<>();

    // Access count for promotion decisions
    private final Map<String, Integer> accessCount = new HashMap<>();
    private final int PROMOTION_THRESHOLD = 3;

    // Statistics
    private int l1Hits, l2Hits, l3Hits, totalRequests = 0;

    public Problem10_MultiLevelCache() {
        // L1: access-order LRU
        l1Cache = new LinkedHashMap<>(L1_CAPACITY, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, VideoData> eldest) {
                return size() > L1_CAPACITY;
            }
        };
        // L2: access-order LRU
        l2Cache = new LinkedHashMap<>(L2_CAPACITY, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, VideoData> eldest) {
                return size() > L2_CAPACITY;
            }
        };
    }

    /** Seed database with videos */
    public void seedDatabase(String... videoIds) {
        for (String id : videoIds) {
            l3Database.put(id, new VideoData(id, "Video: " + id));
        }
    }

    /** Pre-populate L2 with some videos */
    public void addToL2(String videoId) {
        VideoData data = l3Database.get(videoId);
        if (data != null) l2Cache.put(videoId, data);
    }

    /** Get a video - checks L1 -> L2 -> L3 */
    public void getVideo(String videoId) {
        totalRequests++;
        accessCount.merge(videoId, 1, Integer::sum);
        int count = accessCount.get(videoId);

        System.out.println("getVideo(\"" + videoId + "\")");

        // Check L1
        if (l1Cache.containsKey(videoId)) {
            l1Hits++;
            System.out.println("  -> L1 Cache HIT (0.5ms)");
            return;
        }
        System.out.println("  -> L1 Cache MISS (0.5ms)");

        // Check L2
        if (l2Cache.containsKey(videoId)) {
            l2Hits++;
            VideoData data = l2Cache.get(videoId);
            System.out.println("  -> L2 Cache HIT (5ms)");
            // Promote to L1 if access count exceeds threshold
            if (count >= PROMOTION_THRESHOLD) {
                l1Cache.put(videoId, data);
                System.out.println("  -> Promoted to L1 (access count: " + count + ")");
            }
            return;
        }
        System.out.println("  -> L2 Cache MISS");

        // Check L3
        if (l3Database.containsKey(videoId)) {
            l3Hits++;
            VideoData data = l3Database.get(videoId);
            System.out.println("  -> L3 Database HIT (150ms)");
            l2Cache.put(videoId, data);
            System.out.println("  -> Added to L2 (access count: " + count + ")");
        } else {
            System.out.println("  -> NOT FOUND");
        }
    }

    /** Invalidate a video from all cache levels */
    public void invalidate(String videoId) {
        l1Cache.remove(videoId);
        l2Cache.remove(videoId);
        System.out.println("invalidate(\"" + videoId + "\") -> Removed from all cache levels");
    }

    /** Print cache statistics */
    public void getStatistics() {
        int total = l1Hits + l2Hits + l3Hits;
        if (total == 0) { System.out.println("No requests yet"); return; }

        System.out.println("getStatistics() ->");
        System.out.printf("  L1: Hit Rate %d%%, Avg Time: 0.5ms%n",
                (l1Hits * 100 / totalRequests));
        System.out.printf("  L2: Hit Rate %d%%, Avg Time: 5ms%n",
                (l2Hits * 100 / totalRequests));
        System.out.printf("  L3: Hit Rate %d%%, Avg Time: 150ms%n",
                (l3Hits * 100 / totalRequests));
        double avgTime = (l1Hits * 0.5 + l2Hits * 5.0 + l3Hits * 150.0) / totalRequests;
        System.out.printf("  Overall: Hit Rate %d%%, Avg Time: %.1fms%n",
                (total * 100 / totalRequests), avgTime);
    }

    public static void main(String[] args) {
        Problem10_MultiLevelCache cache = new Problem10_MultiLevelCache();

        System.out.println("=== Multi-Level Cache System ===");
        System.out.println();

        // Seed database
        cache.seedDatabase("video_123", "video_456", "video_789", "video_999");

        // Pre-populate L2 with video_123
        cache.addToL2("video_123");
        System.out.println("--- Initial state: video_123 in L2, video_999 only in L3 ---");
        System.out.println();

        // First request: L2 hit
        cache.getVideo("video_123");
        System.out.println();

        // Second request: L2 hit
        cache.getVideo("video_123");
        System.out.println();

        // Third request: triggers promotion to L1
        cache.getVideo("video_123");
        System.out.println();

        // Fourth request: L1 hit
        cache.getVideo("video_123");
        System.out.println("  -> Total: 0.5ms");
        System.out.println();

        // Video only in L3
        cache.getVideo("video_999");
        System.out.println();

        // Statistics
        cache.getStatistics();
    }
}