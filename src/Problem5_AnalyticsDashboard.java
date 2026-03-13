import java.util.*;
import java.util.stream.*;

/**
 * ========================================================
 * Problem 5: Real-Time Analytics Dashboard for Website Traffic
 * ========================================================
 * Concepts: Multiple HashMaps, frequency counting, top-N, unique visitors
 */
public class Problem5_AnalyticsDashboard {

    // pageUrl -> total visit count
    private Map<String, Integer> pageViews = new HashMap<>();

    // pageUrl -> Set of unique userIds
    private Map<String, Set<String>> uniqueVisitors = new HashMap<>();

    // traffic source -> count
    private Map<String, Integer> trafficSources = new HashMap<>();

    // Total events processed
    private int totalEvents = 0;

    /** Process a single page view event */
    public void processEvent(String url, String userId, String source) {
        // Track page views
        pageViews.merge(url, 1, Integer::sum);

        // Track unique visitors per page
        uniqueVisitors.computeIfAbsent(url, k -> new HashSet<>()).add(userId);

        // Track traffic source
        trafficSources.merge(source, 1, Integer::sum);

        totalEvents++;
    }

    /** Display the full analytics dashboard */
    public void getDashboard() {
        System.out.println("\n=== Analytics Dashboard ===");
        System.out.println("Total Events Processed: " + totalEvents);
        System.out.println();

        // Top 10 pages by views
        System.out.println("Top Pages:");
        pageViews.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(10)
                .forEach(e -> {
                    int unique = uniqueVisitors.getOrDefault(e.getKey(), Collections.emptySet()).size();
                    System.out.printf("  %-40s - %,d views (%,d unique)%n",
                            e.getKey(), e.getValue(), unique);
                });

        System.out.println();

        // Traffic sources as percentages
        System.out.println("Traffic Sources:");
        trafficSources.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(e -> {
                    double pct = e.getValue() * 100.0 / totalEvents;
                    System.out.printf("  %-12s: %.0f%%%n", e.getKey(), pct);
                });
    }

    public static void main(String[] args) {
        Problem5_AnalyticsDashboard dashboard = new Problem5_AnalyticsDashboard();

        System.out.println("=== Real-Time Website Analytics ===");

        // Simulate page view events
        String[] pages = {
                "/article/breaking-news", "/sports/championship",
                "/tech/ai-trends", "/world/politics", "/entertainment/movies"
        };
        String[] sources = {"google", "facebook", "direct", "twitter", "other"};
        Random rand = new Random(42);

        // Simulate events with weighted distribution
        int[] weights = {15423, 12091, 8432, 6210, 4890};
        for (int p = 0; p < pages.length; p++) {
            for (int i = 0; i < weights[p]; i++) {
                String userId = "user_" + rand.nextInt(10000);
                String source = sources[rand.nextInt(sources.length)];
                dashboard.processEvent(pages[p], userId, source);
            }
        }

        // Show dashboard
        dashboard.getDashboard();
    }
}