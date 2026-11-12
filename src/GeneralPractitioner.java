import java.time.DayOfWeek;
import java.util.Set;

/**
 * Part 2 - a general practitioner, the one type the brief requires.
 *
 * The class only adds what is unique to a GP: whether the GP is accepting new
 * patients. Everything else is inherited from Doctor and HealthProfessional.
 */
public class GeneralPractitioner extends Doctor {

    private final boolean acceptingNewPatients;

    public GeneralPractitioner(String id, String name, Set<DayOfWeek> workingDays,
                               String practitionerNumber, boolean acceptingNewPatients) {
        super(id, name, workingDays, practitionerNumber);
        this.acceptingNewPatients = acceptingNewPatients;
    }

    /** Every GP created with this constructor is accepting new patients by default. */
    public GeneralPractitioner(String id, String name, String practitionerNumber) {
        super(id, name, practitionerNumber);
        this.acceptingNewPatients = true;
    }

    public boolean isAcceptingNewPatients() {
        return acceptingNewPatients;
    }

    @Override
    public String getRole() {
        return "General Practitioner";
    }

    @Override
    public String performDuty() {
        return "conducts general consultations, diagnoses common conditions and prescribes medication";
    }

    @Override
    public String toString() {
        return super.toString() + System.lineSeparator()
                + "           Accepting new patients: " + (acceptingNewPatients ? "yes" : "no");
    }
}
