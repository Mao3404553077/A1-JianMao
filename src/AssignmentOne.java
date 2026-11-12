import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * AssignmentOne - the entry point for the PROG2004 Assessment 1 submission.
 *
 * The main method builds a small, scripted simulation of the health service's
 * appointment system. It creates health professionals of several types (Parts 1 to
 * 3), models and prints an appointment (Part 4), manages a collection of
 * appointments (Part 5) and demonstrates the whole system end to end, including
 * the cases that must fail (Part 6).
 *
 * Run with:  java AssignmentOne
 */
public class AssignmentOne {

    private static final String HEAVY =
            "================================================================================";
    private static final String LIGHT =
            "--------------------------------------------------------------------------------";

    public static void main(String[] args) {
        printHeader();

        List<HealthProfessional> professionals = createHealthProfessionals();
        List<Patient> patients = createPatients();

        demonstratePartThree(professionals);
        demonstratePartFour(professionals, patients);
        demonstratePartsFiveAndSix(professionals, patients);

        System.out.println();
        System.out.println(HEAVY);
        System.out.println(" End of demonstration.");
        System.out.println(HEAVY);
    }

    // ------------------------------------------------------------------
    // Part 3 - working with health professional objects
    // ------------------------------------------------------------------

    private static void demonstratePartThree(List<HealthProfessional> professionals) {
        section("PART 3 - Working with health professional objects");

        System.out.println(" " + professionals.size()
                + " professionals of " + countProfessionalTypes(professionals)
                + " concrete types are stored together in one List<HealthProfessional>.");
        System.out.println(" Printing each object calls its own toString(), which adds the role-specific");
        System.out.println(" details to the shared details inherited from HealthProfessional:");
        System.out.println();
        for (HealthProfessional professional : professionals) {
            System.out.println(" " + professional);
            System.out.println();
        }

        step("The same objects respond in their own way when handled as HealthProfessional");
        for (HealthProfessional professional : professionals) {
            System.out.println("  " + professional.getName() + " (" + professional.getRole()
                    + "): " + professional.performDuty());
        }

        step("Behaviour that only one specific type has");
        for (HealthProfessional professional : professionals) {
            if (professional instanceof Doctor) {
                System.out.println("  " + ((Doctor) professional).prescribe("Amoxicillin"));
            } else if (professional instanceof Nurse) {
                System.out.println("  " + ((Nurse) professional).takeBloodSample());
            }
        }

        System.out.println();
        System.out.println("  Adding a fifth type would mean one new subclass and no change to this loop,");
        System.out.println("  to the list, or to any of the appointment code.");
    }

    // ------------------------------------------------------------------
    // Part 4 - modelling an appointment
    // ------------------------------------------------------------------

    private static void demonstratePartFour(List<HealthProfessional> professionals,
                                            List<Patient> patients) {
        section("PART 4 - Modelling an appointment");

        Patient alice = patients.get(0);
        Patient ben = patients.get(1);
        HealthProfessional gpNguyen = findProfessional(professionals, "1001");
        HealthProfessional nurseLee = findProfessional(professionals, "3001");

        Appointment first = new Appointment(alice, gpNguyen, LocalTime.of(9, 0));
        Appointment second = new Appointment(ben, nurseLee, LocalTime.of(9, 30));

        System.out.println(" One Appointment class links a patient, a health professional and a time:");
        System.out.println("   " + first);
        System.out.println("   " + second);
        System.out.println();
        System.out.println(" The professional attribute is a HealthProfessional, so the same class accepted");
        System.out.println(" a GP and a nurse, and printing an appointment shows the professional's details.");
        System.out.println();
        System.out.println(" Appointment implements Comparable, so a collection can be put in time order:");
        System.out.println("   first.compareTo(second) = " + first.compareTo(second)
                + "  (negative, meaning the first appointment is earlier)");
        System.out.println();
        System.out.println(" The time is stored as a LocalTime, not a String, so comparing and sorting");
        System.out.println(" times is correct without any extra parsing.");
    }

    // ------------------------------------------------------------------
    // Parts 5 and 6 - managing and demonstrating appointments
    // ------------------------------------------------------------------

    private static void demonstratePartsFiveAndSix(List<HealthProfessional> professionals,
                                                   List<Patient> patients) {
        AppointmentManager manager = new AppointmentManager();

        Patient alice = patients.get(0);
        Patient ben = patients.get(1);
        Patient chloe = patients.get(2);
        Patient david = patients.get(3);

        HealthProfessional gpNguyen = findProfessional(professionals, "1001");
        HealthProfessional gpCarter = findProfessional(professionals, "1002");
        HealthProfessional gpMartin = findProfessional(professionals, "1003");
        HealthProfessional cardiologist = findProfessional(professionals, "2001");
        HealthProfessional nurseLee = findProfessional(professionals, "3001");
        HealthProfessional nurseAdams = findProfessional(professionals, "3002");
        HealthProfessional dietitianBrown = findProfessional(professionals, "4001");

        section("PART 5 - Managing the collection of appointments");
        printDailySchedule();

        step("Booking appointments for today (all required information is checked)");
        report(manager.addAppointment(alice, gpNguyen, LocalTime.of(9, 0)));
        report(manager.addAppointment(ben, nurseLee, LocalTime.of(9, 0)));
        report(manager.addAppointment(chloe, cardiologist, LocalTime.of(10, 30)));
        report(manager.addAppointment(alice, dietitianBrown, LocalTime.of(11, 0)));
        report(manager.addAppointment(ben, gpCarter, LocalTime.of(14, 0)));
        report(manager.addAppointment(chloe, nurseAdams, LocalTime.of(15, 30)));
        report(manager.addAppointment(david, gpMartin, LocalTime.of(8, 0)));

        step("Every appointment, in the order it was booked");
        manager.printAppointments(" Current appointments:", manager.getAllAppointments());
        System.out.println(" Total appointments: " + manager.size());

        step("Filtering by health professional ID");
        manager.printAppointments(" Appointments for professional ID " + gpNguyen.getId() + ":",
                manager.findByProfessionalId(gpNguyen.getId()));

        step("Filtering by patient mobile number");
        manager.printAppointments(" Appointments for " + ben.getFormattedMobileNumber() + ":",
                manager.findByPatientMobileNumber(ben.getMobileNumber()));

        step("Sorting: the filtered subset, earliest to latest");
        manager.printAppointments(" Appointments for " + ben.getFormattedMobileNumber()
                        + ", sorted:",
                manager.sortByTime(manager.findByPatientMobileNumber(ben.getMobileNumber())));

        step("Sorting: all appointments, earliest to latest");
        manager.printAppointments(" All appointments, sorted:", manager.getAllAppointmentsSortedByTime());

        section("PART 6 - Working with appointments: cancellations and error cases");

        step("Operations that cannot be performed return a warning");
        report(manager.addAppointment(alice, gpNguyen, LocalTime.of(9, 0)));
        report(manager.addAppointment(ben, gpNguyen, LocalTime.of(8, 15)));
        report(manager.addAppointment(ben, gpNguyen, LocalTime.of(18, 0)));
        HealthProfessional locum = new GeneralPractitioner("1099", "Dr Jack Wilson",
                rosterForTheNextTwoDays(), "MED-1099", true);
        report(manager.addAppointment(david, locum, LocalTime.of(13, 0)));
        report(manager.addAppointment(null, gpNguyen, LocalTime.of(13, 0)));
        report(manager.cancelAppointment(gpNguyen.getId(), LocalTime.of(15, 30)));
        report(manager.cancelAppointmentsForPatient("0400 000 000"));

        step("Cancelling an appointment by professional ID and time");
        report(manager.cancelAppointment(gpNguyen.getId(), LocalTime.of(9, 0)));
        manager.printAppointments(" Appointments after that cancellation:",
                manager.getAllAppointmentsSortedByTime());
        System.out.println(" Total appointments: " + manager.size());

        step("Cancelling every appointment for a patient by mobile number");
        report(manager.cancelAppointmentsForPatient(ben.getFormattedMobileNumber()));
        manager.printAppointments(" Appointments after that cancellation:",
                manager.getAllAppointmentsSortedByTime());
        System.out.println(" Total appointments: " + manager.size());

        step("Part 5 and Part 6 functionality that has been demonstrated");
        System.out.println("  - add an appointment, with all required information checked");
        System.out.println("  - reject a booking that would double-book a professional");
        System.out.println("  - reject a time that is not a clinic slot, or is outside opening hours");
        System.out.println("  - reject a professional who does not work today");
        System.out.println("  - view all appointments, and filter by professional ID or mobile number");
        System.out.println("  - sort all appointments, and sort a filtered subset, by time");
        System.out.println("  - cancel by professional ID and time, and cancel by mobile number");
        System.out.println("  - display the updated collection after each cancellation");
    }

    // ------------------------------------------------------------------
    // Object creation and small helpers
    // ------------------------------------------------------------------

    /**
     * Creates the health professionals used in the demonstration: three general
     * practitioners, two specialists, two nurses and one dietitian. All of them are
     * stored as HealthProfessional, which is what lets the rest of the program treat
     * them in the same way.
     */
    private static List<HealthProfessional> createHealthProfessionals() {
        List<HealthProfessional> professionals = new ArrayList<>();
        professionals.add(new GeneralPractitioner("1001", "Dr Alice Nguyen",
                rosterIncludingToday(), "MED-1001", true));
        professionals.add(new GeneralPractitioner("1002", "Dr Ben Carter",
                rosterIncludingToday(), "MED-1002", false));
        professionals.add(new GeneralPractitioner("1003", "Dr Chloe Martin",
                rosterIncludingToday(), "MED-1003", true));
        professionals.add(new Specialist("2001", "Dr Daniel Okafor",
                rosterIncludingToday(), "MED-2001", "Cardiology"));
        professionals.add(new Specialist("2002", "Dr Emma Rossi",
                rosterIncludingToday(), "MED-2002", "Paediatrics"));
        professionals.add(new Nurse("3001", "Grace Lee",
                rosterIncludingToday(), "Ward 3B"));
        professionals.add(new Nurse("3002", "Henry Adams",
                rosterIncludingToday(), "Ward 5A"));
        professionals.add(new Dietitian("4001", "Isla Brown",
                rosterIncludingToday(), "Sports nutrition"));
        return professionals;
    }

    private static List<Patient> createPatients() {
        List<Patient> patients = new ArrayList<>();
        patients.add(new Patient("Alice Wong", "0412 345 678"));
        patients.add(new Patient("Ben Smith", "0423456789"));
        patients.add(new Patient("Chloe Tan", "+61 434 567 890"));
        patients.add(new Patient("David Kim", "0455 678 901"));
        return patients;
    }

    /**
     * Monday to Friday, plus today. Bookings can only be made for the current day,
     * so the demonstration always includes today in the roster, whatever day the
     * program is run on. The roster itself is an EnumSet, a collection designed for
     * a fixed set of enum values.
     */
    private static EnumSet<DayOfWeek> rosterIncludingToday() {
        EnumSet<DayOfWeek> days = EnumSet.range(DayOfWeek.MONDAY, DayOfWeek.FRIDAY);
        days.add(LocalDate.now().getDayOfWeek());
        return days;
    }

    /** A roster that deliberately excludes today, used to test the working-day check. */
    private static EnumSet<DayOfWeek> rosterForTheNextTwoDays() {
        DayOfWeek today = LocalDate.now().getDayOfWeek();
        EnumSet<DayOfWeek> days = EnumSet.noneOf(DayOfWeek.class);
        days.add(today.plus(1));
        days.add(today.plus(2));
        return days;
    }

    private static HealthProfessional findProfessional(List<HealthProfessional> professionals,
                                                       String id) {
        for (HealthProfessional professional : professionals) {
            if (professional.getId().equals(id)) {
                return professional;
            }
        }
        throw new IllegalArgumentException("No health professional with ID " + id);
    }

    /** Counts the concrete classes in use, so two specialists are still one type. */
    private static int countProfessionalTypes(List<HealthProfessional> professionals) {
        List<String> types = new ArrayList<>();
        for (HealthProfessional professional : professionals) {
            String type = professional.getClass().getSimpleName();
            if (!types.contains(type)) {
                types.add(type);
            }
        }
        return types.size();
    }

    private static void printDailySchedule() {
        List<LocalTime> slots = ClinicSchedule.getTimeSlots();
        System.out.println(" The clinic works to a fixed daily schedule, " + ClinicSchedule.describe() + ":");
        StringBuilder line = new StringBuilder("  ");
        for (LocalTime slot : slots) {
            line.append(ClinicSchedule.format(slot)).append("  ");
        }
        System.out.println(line);
        System.out.println("  " + slots.size()
                + " bookable start times; changing ClinicSchedule changes the whole system.");
    }

    private static void printHeader() {
        System.out.println(HEAVY);
        System.out.println(" PROG2004 Object Oriented Programming - Assessment 1");
        System.out.println(" Health Appointment System - scripted console demonstration");
        System.out.println(" Student: Jian Mao (25259162)");
        System.out.println(" Simulation day: " + LocalDate.now() + " ("
                + LocalDate.now().getDayOfWeek() + ")");
        System.out.println(" Bookings are only made for the current day.");
        System.out.println(HEAVY);
    }

    private static void section(String title) {
        System.out.println();
        System.out.println(HEAVY);
        System.out.println(" " + title);
        System.out.println(HEAVY);
    }

    private static void step(String title) {
        System.out.println();
        System.out.println(" " + title);
        System.out.println(LIGHT);
    }

    private static void report(String message) {
        System.out.println("   " + message);
    }
}
