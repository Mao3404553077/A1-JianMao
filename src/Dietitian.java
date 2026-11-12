import java.time.DayOfWeek;
import java.util.Set;

/**
 * Part 2 - a dietitian, the fourth specific type.
 *
 * This class exists mainly to show that adding a new kind of health professional
 * costs one small subclass and no change to the appointment code, because
 * appointments refer to the HealthProfessional supertype.
 */
public class Dietitian extends HealthProfessional {

    private final String dietaryFocus;

    public Dietitian(String id, String name, Set<DayOfWeek> workingDays, String dietaryFocus) {
        super(id, name, workingDays);
        this.dietaryFocus = validateFocus(dietaryFocus);
    }

    public Dietitian(String id, String name, String dietaryFocus) {
        super(id, name);
        this.dietaryFocus = validateFocus(dietaryFocus);
    }

    public String getDietaryFocus() {
        return dietaryFocus;
    }

    @Override
    public String getRole() {
        return "Dietitian";
    }

    @Override
    public String performDuty() {
        return "assesses nutritional needs and prepares dietary plans for " + dietaryFocus;
    }

    @Override
    public String toString() {
        return super.toString() + System.lineSeparator()
                + "           Dietary focus: " + dietaryFocus;
    }

    private static String validateFocus(String focus) {
        String trimmed = (focus == null) ? "" : focus.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("A dietitian must have a dietary focus.");
        }
        return trimmed;
    }
}
