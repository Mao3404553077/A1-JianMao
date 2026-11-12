import java.time.DayOfWeek;
import java.util.Set;

/**
 * Part 2 - the shared superclass for medical doctors.
 *
 * This extra level in the hierarchy shows that inheritance can be more than one
 * level deep: GeneralPractitioner and Specialist both reuse the code in
 * HealthProfessional and the prescribing behaviour defined here, and neither of
 * them repeats it.
 */
public abstract class Doctor extends HealthProfessional {

    private final String practitionerNumber;

    protected Doctor(String id, String name, Set<DayOfWeek> workingDays, String practitionerNumber) {
        super(id, name, workingDays);
        this.practitionerNumber = validatePractitionerNumber(practitionerNumber);
    }

    /** Convenience constructor: doctors in this clinic work Monday to Friday. */
    protected Doctor(String id, String name, String practitionerNumber) {
        super(id, name);
        this.practitionerNumber = validatePractitionerNumber(practitionerNumber);
    }

    public String getPractitionerNumber() {
        return practitionerNumber;
    }

    /**
     * Behaviour that is common to every doctor and that the clinic asked for:
     * doctors can prescribe drugs. The method returns a message instead of printing,
     * so the caller decides how the result is presented.
     */
    public String prescribe(String medicationName) {
        String medication = (medicationName == null) ? "" : medicationName.trim();
        if (medication.isEmpty()) {
            throw new IllegalArgumentException("A medication name is required to write a prescription.");
        }
        return getName() + " writes a prescription for " + medication
                + " (registration " + practitionerNumber + ").";
    }

    @Override
    public String toString() {
        return super.toString() + System.lineSeparator()
                + "           Registration: " + practitionerNumber;
    }

    private static String validatePractitionerNumber(String practitionerNumber) {
        String trimmed = (practitionerNumber == null) ? "" : practitionerNumber.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("A doctor must have a practitioner registration number.");
        }
        return trimmed;
    }
}
