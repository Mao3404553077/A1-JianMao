import java.time.LocalTime;
import java.util.Objects;

/**
 * Part 4 - one appointment, linking a patient, a health professional and a time.
 *
 * The professional field is declared as HealthProfessional, so a single
 * appointment class works with every type created in Part 2. At run time the
 * object is still the concrete type (a GP, a nurse and so on), which is
 * polymorphism in action.
 *
 * Appointment implements Comparable so a whole collection can be sorted from
 * earliest to latest. The time is stored as LocalTime (not as a String) because
 * LocalTime already knows how to compare two times correctly.
 */
public class Appointment implements Comparable<Appointment> {

    private final Patient patient;
    private final HealthProfessional professional;
    private final LocalTime time;

    public Appointment(Patient patient, HealthProfessional professional, LocalTime time) {
        if (patient == null) {
            throw new IllegalArgumentException("An appointment needs a patient.");
        }
        if (professional == null) {
            throw new IllegalArgumentException("An appointment needs a health professional.");
        }
        if (time == null) {
            throw new IllegalArgumentException("An appointment needs a time.");
        }
        if (!ClinicSchedule.isValidTimeSlot(time)) {
            throw new IllegalArgumentException("The time " + ClinicSchedule.format(time)
                    + " is not a clinic slot (" + ClinicSchedule.describe() + ").");
        }
        this.patient = patient;
        this.professional = professional;
        this.time = time;
    }

    public Patient getPatient() {
        return patient;
    }

    public HealthProfessional getProfessional() {
        return professional;
    }

    public LocalTime getTime() {
        return time;
    }

    /**
     * Two appointments clash when they are for the same professional at the same
     * time, whether or not the same patient is involved. This is the test the
     * booking manager uses to stop double-booking, and it relies on
     * HealthProfessional.equals.
     */
    public boolean clashesWith(Appointment other) {
        return other != null && time.equals(other.time)
                && professional.equals(other.professional);
    }

    /**
     * Orders appointments by time. When two appointments start at the same time,
     * the professional ID breaks the tie so the output is stable; the brief allows
     * any order for the same time and different professionals.
     */
    @Override
    public int compareTo(Appointment other) {
        int byTime = time.compareTo(other.time);
        if (byTime != 0) {
            return byTime;
        }
        return professional.getId().compareTo(other.professional.getId());
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Appointment that = (Appointment) other;
        return time.equals(that.time) && professional.equals(that.professional);
    }

    @Override
    public int hashCode() {
        return Objects.hash(professional, time);
    }

    /**
     * Printing an appointment shows the time, the patient and the chosen health
     * professional, including what kind of professional they are.
     */
    @Override
    public String toString() {
        return String.format("%s | %-26s | %s %s (ID %s)",
                ClinicSchedule.format(time),
                patient.toString(),
                professional.getRole(),
                professional.getName(),
                professional.getId());
    }
}