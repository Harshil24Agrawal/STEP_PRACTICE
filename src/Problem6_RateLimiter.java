import java.util.*;

/**
 * ========================================================
 * Problem 6: Distributed Rate Limiter for API Gateway
 * ========================================================
 * Concepts: Token bucket, HashMap client tracking, time-based ops
 */
public class Problem6_RateLimiter {

    /** Token bucket per client */
    static class TokenBucket {
        int tokens;
        long lastRefillTime;
        int maxTokens;
        int refillRate;       // tokens per hour
        long windowMillis;    // 1 hour in ms

        TokenBucket(int maxTokens) {
            this.maxTokens = maxTokens;
            this.tokens = maxTokens;
            this.refillRate = maxTokens;
            this.lastRefillTime = System.currentTimeMillis();
            this.windowMillis = 3600_000L; // 1 hour
        }

        /** Refill tokens based on elapsed time */
        void refill() {
            long now = System.currentTimeMillis();
            long elapsed = now - lastRefillTime;
            if (elapsed >= windowMillis) {
                tokens = maxTokens;
                lastRefillTime = now;
            }
        }

        /** Consume one token. Returns true if allowed. */
        synchronized boolean consume() {
            refill();
            if (tokens > 0) {
                tokens--;
                return true;
            }
            return false;
        }

        long retryAfterSeconds() {
            long elapsed = System.currentTimeMillis() - lastRefillTime;
            return Math.max(0, (windowMillis - elapsed) / 1000);
        }
    }

    // clientId -> TokenBucket
    private Map<String, TokenBucket> clientBuckets = new HashMap<>();
    private final int requestsPerHour;

    public Problem6_RateLimiter(int requestsPerHour) {
        this.requestsPerHour = requestsPerHour;
    }

    /** Check if client request is allowed */
    public String checkRateLimit(String clientId) {
        clientBuckets.putIfAbsent(clientId, new TokenBucket(requestsPerHour));
        TokenBucket bucket = clientBuckets.get(clientId);

        if (bucket.consume()) {
            return "Allowed (" + bucket.tokens + " requests remaining)";
        } else {
            return "Denied (0 requests remaining, retry after " +
                    bucket.retryAfterSeconds() + "s)";
        }
    }

    /** Get current rate limit status for a client */
    public String getRateLimitStatus(String clientId) {
        TokenBucket bucket = clientBuckets.get(clientId);
        if (bucket == null) return "Client not found";
        int used = requestsPerHour - bucket.tokens;
        long resetEpoch = (bucket.lastRefillTime + bucket.windowMillis) / 1000;
        return String.format("{used: %d, limit: %d, reset: %d}",
                used, requestsPerHour, resetEpoch);
    }

    public static void main(String[] args) {
        Problem6_RateLimiter limiter = new Problem6_RateLimiter(1000);

        System.out.println("=== Distributed Rate Limiter ===");
        System.out.println();

        // First two requests - should be allowed
        System.out.println("checkRateLimit(\"abc123\") -> " + limiter.checkRateLimit("abc123"));
        System.out.println("checkRateLimit(\"abc123\") -> " + limiter.checkRateLimit("abc123"));
        System.out.println();

        // Exhaust remaining 998 requests
        System.out.println("... consuming remaining 998 requests ...");
        for (int i = 0; i < 998; i++) limiter.checkRateLimit("abc123");
        System.out.println();

        // Should be denied now
        System.out.println("checkRateLimit(\"abc123\") -> " + limiter.checkRateLimit("abc123"));
        System.out.println();

        System.out.println("getRateLimitStatus(\"abc123\") -> " +
                limiter.getRateLimitStatus("abc123"));
        System.out.println();

        // Different client - fresh bucket
        System.out.println("checkRateLimit(\"xyz789\") -> " + limiter.checkRateLimit("xyz789"));
    }
}