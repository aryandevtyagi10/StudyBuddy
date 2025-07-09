import java.util.; import java.io.;

class Unit { String name; int hours; boolean completed;

Unit(String name, int hours) {
    this.name = name;
    this.hours = hours;
    this.completed = false;
}

}

class Subject { String name; int totalHours; int hoursCompleted = 0; ArrayList<Unit> units = new ArrayList<>();

Subject(String name, int totalHours) {
    this.name = name;
    this.totalHours = totalHours;
}

void addUnit(String unitName, int unitHours) {
    units.add(new Unit(unitName, unitHours));
}

void markUnitCompleted(String unitName) {
    for (Unit u : units) {
        if (u.name.equalsIgnoreCase(unitName)) {
            u.completed = true;
            hoursCompleted += u.hours;
            return;
        }
    }
    System.out.println("Unit not found.");
}

void displayUnits() {
    System.out.println("\nSubject: " + name);
    System.out.println("| Unit Name           | Hours | Completed |");
    System.out.println("|---------------------|--------|-----------|");
    for (Unit u : units) {
        System.out.printf("| %-20s | %-6d | %-9s |\n", u.name, u.hours, u.completed ? "Yes" : "No");
    }
    System.out.println("Total Hours: " + totalHours + ", Completed: " + hoursCompleted);
    displayProgressBar();
}

void displayProgressBar() {
    int progress = (int) (((double) hoursCompleted / totalHours) * 20);
    System.out.print("Progress: [");
    for (int i = 0; i < 20; i++) {
        if (i < progress) System.out.print("#");
        else System.out.print("-");
    }
    System.out.println("] " + (hoursCompleted * 100 / totalHours) + "%");
}

}

public class StudyBuddyPlanner { static Scanner sc = new Scanner(System.in); static Map<String, Subject> subjectMap = new HashMap<>(); static Map<String, String> dateSheet = new HashMap<>(); static long startTime = 0, endTime = 0, totalStudyTime = 0;

public static void main(String[] args) {
    while (true) {
        System.out.println("\n--- StudyBuddy Menu ---");
        System.out.println("1. Add a Subject");
        System.out.println("2. Add Units to a Subject");
        System.out.println("3. Create Custom Date Sheet");
        System.out.println("4. View Study Plan");
        System.out.println("5. Start/Stop Study Timer");
        System.out.println("6. Manually Log Study Hours");
        System.out.println("7. Mark Unit as Completed");
        System.out.println("8. Save Plan to File");
        System.out.println("9. Exit");
        System.out.print("Choose an option: ");

        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {
            case 1 -> addSubject();
            case 2 -> addUnits();
            case 3 -> createDateSheet();
            case 4 -> viewStudyPlan();
            case 5 -> trackStudyTime();
            case 6 -> manualLog();
            case 7 -> markUnitComplete();
            case 8 -> savePlanToFile();
            case 9 -> System.exit(0);
            default -> System.out.println("Invalid option");
        }
    }
}

static void addSubject() {
    System.out.print("Enter subject name: ");
    String name = sc.nextLine();
    System.out.print("Enter total hours assigned: ");
    int hours = sc.nextInt();
    sc.nextLine();
    subjectMap.put(name, new Subject(name, hours));
}

static void addUnits() {
    System.out.print("Enter subject name: ");
    String subject = sc.nextLine();
    if (!subjectMap.containsKey(subject)) {
        System.out.println("Subject not found.");
        return;
    }
    Subject sub = subjectMap.get(subject);
    System.out.print("Enter number of units: ");
    int n = sc.nextInt();
    sc.nextLine();
    for (int i = 0; i < n; i++) {
        System.out.print("Enter unit name: ");
        String unitName = sc.nextLine();
        System.out.print("Enter hours for this unit: ");
        int unitHours = sc.nextInt();
        sc.nextLine();
        sub.addUnit(unitName, unitHours);
    }
}

static void createDateSheet() {
    System.out.print("Enter number of exam days: ");
    int days = sc.nextInt();
    sc.nextLine();
    for (int i = 0; i < days; i++) {
        System.out.print("Enter date (e.g., 10-July): ");
        String date = sc.nextLine();
        System.out.print("Enter subject to study: ");
        String subject = sc.nextLine();
        dateSheet.put(date, subject);
    }
}

static void viewStudyPlan() {
    for (Subject s : subjectMap.values()) {
        s.displayUnits();
    }

    System.out.println("--- Custom Date Sheet ---");
    for (String date : dateSheet.keySet()) {
        System.out.println(date + " -> " + dateSheet.get(date));
    }
    System.out.println("Total Study Time Recorded: " + totalStudyTime / 1000 + " seconds");
}

static void trackStudyTime() {
    if (startTime == 0) {
        System.out.println("\nStudy Timer Started.");
        startTime = System.currentTimeMillis();
    } else {
        endTime = System.currentTimeMillis();
        long sessionTime = endTime - startTime;
        totalStudyTime += sessionTime;
        startTime = 0;
        System.out.println("\nStudy Timer Stopped. Session Time: " + sessionTime / 1000 + " seconds");
    }
}

static void manualLog() {
    System.out.print("Enter subject name: ");
    String name = sc.nextLine();
    if (!subjectMap.containsKey(name)) {
        System.out.println("Subject not found.");
        return;
    }
    Subject sub = subjectMap.get(name);
    System.out.print("Enter hours to log: ");
    int hrs = sc.nextInt();
    sc.nextLine();
    sub.hoursCompleted += hrs;
    System.out.println("Logged " + hrs + " hours to " + name);
}

static void markUnitComplete() {
    System.out.print("Enter subject name: ");
    String subject = sc.nextLine();
    if (!subjectMap.containsKey(subject)) {
        System.out.println("Subject not found.");
        return;
    }
    Subject sub = subjectMap.get(subject);
    System.out.print("Enter unit name to mark completed: ");
    String unit = sc.nextLine();
    sub.markUnitCompleted(unit);
}

static void savePlanToFile() {
    try (PrintWriter writer = new PrintWriter("study_data.txt")) {
        for (Subject s : subjectMap.values()) {
            writer.println("Subject: " + s.name + " (Total Hours: " + s.totalHours + ", Completed: " + s.hoursCompleted + ")");
            for (Unit u : s.units) {
                writer.println("- Unit: " + u.name + ", Hours: " + u.hours + ", Completed: " + (u.completed ? "Yes" : "No"));
            }
            writer.println();
        }
        writer.println("Custom Date Sheet:");
        for (String date : dateSheet.keySet()) {
            writer.println(date + " -> " + dateSheet.get(date));
        }
        writer.println("Total Study Time (seconds): " + totalStudyTime / 1000);
        System.out.println("Study plan saved to study_data.txt\n");
    } catch (IOException e) {
        System.out.println("Error saving file: " + e.getMessage());
    }
}

}

