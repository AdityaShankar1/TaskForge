package TaskForge; // ✅ If you're using a package, this must be first

import java.util.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TaskForge {

    static class Task {
        String id;
        int priority;           // 1–5
        int deadlineHours;      // time left
        String riskLevel;       // Low, Medium, High
        int cost;               // 100–1000
        double score;

        public Task(String id, int priority, int deadlineHours, String riskLevel, int cost) {
            this.id = id;
            this.priority = priority;
            this.deadlineHours = deadlineHours;
            this.riskLevel = riskLevel;
            this.cost = cost;
            this.score = calculateScore();
        }

        private double calculateScore() {
            int riskValue = switch (riskLevel) {
                case "Low" -> 1;
                case "Medium" -> 2;
                case "High" -> 3;
                default -> 2;
            };
            return 0.4 * priority +
                   0.3 * (1.0 / deadlineHours) +
                   0.2 * (1.0 / riskValue) -
                   0.1 * (cost / 1000.0); // Normalize cost
        }

        @Override
        public String toString() {
            return String.format("%s → Score: %.3f | Priority: %d | Deadline: %dh | Risk: %s | Cost: $%d",
                    id, score, priority, deadlineHours, riskLevel, cost);
        }
    }

    public static void main(String[] args) {
        System.out.println("📦 Welcome to TaskForge - Smart Task Prioritization Engine");
        System.out.println("Generating tasks...\n");

        List<Task> tasks = generateRandomTasks(10);
        tasks.sort((t1, t2) -> {
            if (Double.compare(t2.score, t1.score) != 0) {
                return Double.compare(t2.score, t1.score);
            } else {
                return Integer.compare(t1.cost, t2.cost); // Tie-breaker: lower cost wins
            }
        });

        System.out.println("🏁 Top Prioritized Tasks:\n");
        for (int i = 0; i < 5; i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
        forecastDemand();

        writeToFile(tasks.subList(0, 5));
    }

    static List<Task> generateRandomTasks(int count) {
        Random rand = new Random();
        String[] riskLevels = {"Low", "Medium", "High"};
        List<Task> tasks = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            String id = "T" + String.format("%02d", i);
            int priority = rand.nextInt(5) + 1;           // 1 to 5
            int deadline = rand.nextInt(24) + 1;          // 1 to 24 hours
            String risk = riskLevels[rand.nextInt(3)];
            int cost = rand.nextInt(901) + 100;           // 100 to 1000
            tasks.add(new Task(id, priority, deadline, risk, cost));
        }

        return tasks;
    }

    static void writeToFile(List<Task> topTasks) {
        try {
            String filename = "taskforge_schedule.txt";
            FileWriter fw = new FileWriter(filename);

            fw.write("📄 TaskForge Prioritization Report\n");
            fw.write("Generated on: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + "\n\n");
            fw.write("Ranked Tasks:\n");

            int i = 1;
            for (Task t : topTasks) {
                fw.write(i++ + ". " + t.toString() + "\n");
            }
            double totalScore = 0;
            for (Task t : topTasks) {
                totalScore += t.score;
            }
            double avgScore = totalScore / topTasks.size();

            System.out.printf("\n📊 Average Score of Top 5: %.3f\n", avgScore);

            fw.write("\n📊 Average Score: " + String.format("%.3f", avgScore) + "\n");

            fw.close();
            System.out.println("\n📁 Results saved to: " + filename);
        } catch (IOException e) {
            System.out.println("❌ Error writing to file.");
        }
    }
    static void forecastDemand() {
        System.out.println("\n📈 Demand Forecasting Using Moving Average:\n");

        String[] taskTypes = {"Packaging", "Support", "Procurement", "Inventory", "Billing"};
        Random rand = new Random();
        Map<String, int[]> demandHistory = new HashMap<>();
        Map<String, Double> forecast = new HashMap<>();

        // Simulate past 5 days of demand
        for (String task : taskTypes) {
            int[] history = new int[5];
            for (int i = 0; i < 5; i++) {
                history[i] = rand.nextInt(20) + 5; // demand between 5 and 24
            }
            demandHistory.put(task, history);

            // Calculate 3-day moving average
            double avg = (history[2] + history[3] + history[4]) / 3.0;
            forecast.put(task, avg);
        }

        // Print results
        for (String task : taskTypes) {
            System.out.print(task + " → Demand: ");
            for (int n : demandHistory.get(task)) System.out.print(n + " ");
            System.out.printf(" | Forecast (3-day MA): %.2f\n", forecast.get(task));
        }

        // Find max
        String top = Collections.max(forecast.entrySet(), Map.Entry.comparingByValue()).getKey();
        System.out.println("\n🔮 Highest projected demand: " + top + " 📌");
    }

}

