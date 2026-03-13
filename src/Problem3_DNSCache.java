import java.util.*;

/**
 * ========================================================
 * Problem 3: DNS Cache with TTL (Time To Live)
 * ========================================================
 * Concepts: Custom Entry class, TTL expiration, hit/miss stats, LRU eviction
 */
public class Problem3_DNSCache {

    /** DNS Cache Entry with TTL */
    static class DNSEntry {
        String domain;
        String ipAddress;
        long insertedAt;   // System.currentTimeMillis()
        long ttlMillis;    // TTL in milliseconds

        DNSEntry(String domain, String ipAddress, long ttlSeconds) {
            this.domain = domain;
            this.ipAddress = ipAddress;
            this.insertedAt = System.currentTimeMillis();
            this.ttlMillis = ttlSeconds * 1000;
        }

        boolean isExpired() {
            return System.currentTimeMillis() - insertedAt > ttlMillis;
        }

        long remainingTTL() {
            long remaining = ttlMillis - (System.currentTimeMillis() - insertedAt);
            return Math.max(0, remaining / 1000);
        }
    }

    private final int maxSize;
    // LinkedHashMap with access-order for LRU eviction
    private final LinkedHashMap<String, DNSEntry> cache;
    private int hits = 0;
    private int misses = 0;
    private int expirations = 0;
    private long totalLookupTime = 0;
    private int totalLookups = 0;

    // Simulated upstream DNS (domain -> IP)
    private final Map<String, String> upstreamDNS = new HashMap<>();

    public Problem3_DNSCache(int maxSize) {
        this.maxSize = maxSize;
        this.cache = new LinkedHashMap<>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> eldest) {
                return size() > maxSize;
            }
        };
        // Seed upstream DNS
        upstreamDNS.put("google.com", "172.217.14.206");
        upstreamDNS.put("facebook.com", "157.240.221.35");
        upstreamDNS.put("amazon.com", "205.251.242.103");
    }

    /** Resolve a domain - checks cache first, then queries upstream */
    public String resolve(String domain) {
        long start = System.nanoTime();
        totalLookups++;
        String result;

        DNSEntry entry = cache.get(domain);
        if (entry != null && !entry.isExpired()) {
            hits++;
            long elapsed = (System.nanoTime() - start) / 1_000_000;
            totalLookupTime += elapsed;
            result = "Cache HIT -> " + entry.ipAddress +
                    " (TTL remaining: " + entry.remainingTTL() + "s)";
        } else {
            if (entry != null) {
                expirations++;
                cache.remove(domain);
                result = queryUpstream(domain, "Cache EXPIRED");
            } else {
                misses++;
                result = queryUpstream(domain, "Cache MISS");
            }
            long elapsed = (System.nanoTime() - start) / 1_000_000;
            totalLookupTime += elapsed;
        }
        return result;
    }

    private String queryUpstream(String domain, String reason) {
        String ip = upstreamDNS.getOrDefault(domain, "NXDOMAIN");
        long ttl = 300L;
        cache.put(domain, new DNSEntry(domain, ip, ttl));
        return reason + " -> Query upstream -> " + ip + " (TTL: " + ttl + "s)";
    }

    /** Get cache statistics */
    public void getCacheStats() {
        int total = hits + misses + expirations;
        double hitRate = total > 0 ? (hits * 100.0 / total) : 0;
        double avgTime = totalLookups > 0 ? (totalLookupTime * 1.0 / totalLookups) : 0;
        System.out.printf("Hit Rate: %.1f%%, Avg Lookup Time: %.1fms%n", hitRate, avgTime);
        System.out.println("Hits: " + hits + " | Misses: " + misses +
                " | Expirations: " + expirations);
    }

    public static void main(String[] args) throws InterruptedException {
        Problem3_DNSCache dns = new Problem3_DNSCache(1000);

        System.out.println("=== DNS Cache with TTL ===");
        System.out.println();

        System.out.println("resolve(\"google.com\")      -> " + dns.resolve("google.com"));
        System.out.println("resolve(\"google.com\")      -> " + dns.resolve("google.com"));
        System.out.println("resolve(\"facebook.com\")    -> " + dns.resolve("facebook.com"));
        System.out.println("resolve(\"amazon.com\")      -> " + dns.resolve("amazon.com"));
        System.out.println("resolve(\"facebook.com\")    -> " + dns.resolve("facebook.com"));
        System.out.println("resolve(\"amazon.com\")      -> " + dns.resolve("amazon.com"));
        System.out.println();

        System.out.print("getCacheStats() -> ");
        dns.getCacheStats();
    }
}