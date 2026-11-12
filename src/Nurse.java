import java.time.DayOfWeek;
import java.util.Set;

/**
 * Part 2 - a nurse.
 *
 * A nurse is not a doctor, so this class extends HealthProfessional directly and
 * gains the shared ID, name and roster without inheriting prescribing behaviour.
 */
public class Nurse extends HealthProfessional {

    private final String assignedWard;

    public Nurse(String id, String name, Set<DayOfWeek> workingDays, String assignedWard) {
        super(id, name, workingDays);
        this.assignedWard = validateWard(assignedWard);
    }

    public Nurse(String id, String name, String assignedWard) {
        super(id, name);
        this.assignedWard = validateWard(assignedWard);
    }

    public String getAssignedWard() {
        return assignedWard;
    }

    @Override
    public String getRole() {
        return "Nurse";
    }

    @Override
    public String performDuty() {
        return "takes blood samples, monitors patients and administers medication under supervision";
    }

    /** A behaviour that is unique to nurses in this clinic. */
    public String takeBloodSample() {
        return getName() + " takes a blood sample and labels it for the laboratory.";
    }

    @Override
    public String toString() {
        return super.toString() + System.lineSeparator()
                + "           Assigned ward: " + assignedWard;
    }

    private static String validateWard(String ward) {
        String trimmed = (ward == null) ? "" : ward.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("A nurse must be assigned to a ward.");
        }
        return trimmed;
    }
}
