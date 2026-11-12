import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Part 5 - manages the whole collection of appointments in one place.
 *
 * An ArrayList is used because the clinic needs to keep every appointment, view
 * them in any order, filter them and sort them. An ArrayList keeps insertion order,
 * allows two appointments at the same time for different professionals, and is the
 * simplest structure that supports all of the required operations. A HashSet would
 * lose the order needed for the sorted view, and a HashMap is not required because
 * appointments are already unique by professional and time.
 *
 * Every operation returns a String: either a confirmation message or a warning
 * message when the action cannot be performed, as the brief asks for.
 */
public class AppointmentManager {

    private final List<Appointment> appointments = new ArrayList<>();

    /**
     * Books an appointment after checking that the booking is complete, that the
     * time is a real clinic slot, that the professional works today and that the
     * professional is not already booked at that time.
     *
     * @return a confirmation message, or a warning message explaining the problem
     */
    public String addAppointment(Patient patient, HealthProfessional professional, LocalTime time) {
        if (patient == null || professional == null || time == null) {
            return "Warning: a patient, a health professional and a time are all required "
                    + "to book an appointment.";
        }
        if (!ClinicSchedule.isValidTimeSlot(time)) {
            return "Warning: " + ClinicSchedule.format(time) + " is not a clinic time slot ("
                    + ClinicSchedule.describe() + ").";
        }

        // Bookings are only made for the current day, so the professional must be
        // rostered on today.
        DayOfWeek today = LocalDate.now().getDayOfWeek();
        if (!professional.worksOn(today)) {
            return "Warning: " + professional.getName() + " (ID " + professional.getId()
                    + ") does not work on " + formatDay(today) + ".";
        }

        // The same professional must never be booked twice in the same slot.
        Appointment candidate = new Appointment(patient, professional, time);
        for (Appointment existing : appointments) {
            if (existing.clashesWith(candidate)) {
                return "Warning: " + professional.getName() + " (ID " + professional.getId()
                        + ") is already booked at " + ClinicSchedule.format(time) + " for "
                        + existing.getPatient().getName() + ".";
            }
        }

        appointments.add(candidate);
        return "Booking confirmed: " + ClinicSchedule.format(time) + " with "
                + professional.getName() + " (ID " + professional.getId() + ") for "
                + patient.getName() + ".";
    }

    /** All appointments, in the order they were booked. */
    public List<Appointment> getAllAppointments() {
        return Collections.unmodifiableList(new ArrayList<>(appointments));
    }

    /** All appointments, earliest first. */
    public List<Appointment> getAllAppointmentsSortedByTime() {
        return sortByTime(appointments);
    }

    /** Returns a sorted copy, so the caller's list is never changed by this call. */
    public List<Appointment> sortByTime(List<Appointment> source) {
        List<Appointment> sorted = new ArrayList<>(source);
        Collections.sort(sorted);
        return sorted;
    }

    /** Every appointment for one health professional, identified by ID. */
    public List<Appointment> findByProfessionalId(String professionalId) {
        List<Appointment> matches = new ArrayList<>();
        if (professionalId == null) {
            return matches;
        }
        String wantedId = professionalId.trim();
        for (Appointment appointment : appointments) {
            if (appointment.getProfessional().getId().equalsIgnoreCase(wantedId)) {
                matches.add(appointment);
            }
        }
        return matches;
    }

    /** Every appointment for one patient, identified by mobile number. */
    public List<Appointment> findByPatientMobileNumber(String mobileNumber) {
        List<Appointment> matches = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getPatient().matchesMobileNumber(mobileNumber)) {
                matches.add(appointment);
            }
        }
        return matches;
    }

    /**
     * Cancels the appointment for a professional at a time.
     *
     * @return a confirmation, or a warning when no such appointment exists
     */
    public String cancelAppointment(String professionalId, LocalTime time) {
        if (professionalId == null || time == null) {
            return "Warning: a professional ID and an appointment time are required to cancel.";
        }
        String wantedId = professionalId.trim();
        for (int i = 0; i < appointments.size(); i++) {
            Appointment appointment = appointments.get(i);
            if (appointment.getProfessional().getId().equalsIgnoreCase(wantedId)
                    && appointment.getTime().equals(time)) {
                appointments.remove(i);
                return "Appointment cancelled: " + ClinicSchedule.format(time) + " with "
                        + appointment.getProfessional().getName() + " (ID "
                        + appointment.getProfessional().getId() + ") for "
                        + appointment.getPatient().getName() + ".";
            }
        }
        return "Warning: no appointment found for professional ID " + professionalId + " at "
                + ClinicSchedule.format(time) + ".";
    }

    /**
     * Cancels every appointment belonging to one patient. Returning the number of
     * cancelled appointments makes the outcome clear when a patient had several.
     */
    public String cancelAppointmentsForPatient(String mobileNumber) {
        List<Appointment> matches = findByPatientMobileNumber(mobileNumber);
        if (matches.isEmpty()) {
            return "Warning: no appointments found for mobile number " + mobileNumber + ".";
        }
        appointments.removeAll(matches);
        StringBuilder message = new StringBuilder("Appointments cancelled for mobile number ")
                .append(mobileNumber).append(": ").append(matches.size()).append(" appointment(s) removed.");
        for (Appointment appointment : matches) {
            message.append(System.lineSeparator())
                    .append("           - ").append(ClinicSchedule.format(appointment.getTime()))
                    .append(" with ").append(appointment.getProfessional().getName())
                    .append(" (ID ").append(appointment.getProfessional().getId()).append(")");
        }
        return message.toString();
    }

    /** Prints a titled, numbered list, or a clear message when nothing matches. */
    public void printAppointments(String heading, List<Appointment> appointmentsToShow) {
        System.out.println(heading);
        if (appointmentsToShow.isEmpty()) {
            System.out.println("   (no appointments to show)");
            return;
        }
        int position = 1;
        for (Appointment appointment : appointmentsToShow) {
            System.out.printf("   %2d. %s%n", position, appointment);
            position++;
        }
    }

    public int size() {
        return appointments.size();
    }

    /** Turns THURSDAY into Thursday, so warning messages read naturally. */
    private static String formatDay(DayOfWeek day) {
        String name = day.toString();
        return name.charAt(0) + name.substring(1).toLowerCase();
    }
}
