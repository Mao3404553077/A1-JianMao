import java.time.DayOfWeek;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

/**
 * Part 1 - the generic health professional.
 *
 * This abstract class holds the information and behaviour that every kind of
 * health professional has in common: an ID, a name and the days of the week the
 * professional works. It is abstract because a "plain" health professional with no
 * specialty does not exist in the clinic, so only the concrete subclasses created
 * in Part 2 may be instantiated.
 *
 * Encapsulation: every field is private and final. Values are checked in the
 * constructor, and the set of working days is copied both on the way in and on the
 * way out. External code can therefore read a professional's details but can never
 * put an object into an invalid state, nor change a roster behind the object's back.
 */
public abstract class HealthProfessional {

    /** Default roster used by the two-argument constructor: Monday to Friday. */
    private static final EnumSet<DayOfWeek> DEFAULT_WORKING_DAYS =
            EnumSet.range(DayOfWeek.MONDAY, DayOfWeek.FRIDAY);

    /** Short labels used when the roster is printed. Index 0 is Monday. */
    private static final String[] SHORT_DAY_NAMES =
            {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

    private final String id;
    private final String name;
    private final EnumSet<DayOfWeek> workingDays;

    /**
     * Creates a professional with an explicit roster.
     *
     * @param id          the professional's ID, digits only
     * @param name        the professional's full name
     * @param workingDays the days of the week the professional works
     */
    protected HealthProfessional(String id, String name, Set<DayOfWeek> workingDays) {
        this.id = validateId(id);
        this.name = validateName(name);
        this.workingDays = copyWorkingDays(workingDays);
    }

    /**
     * Convenience constructor for a full-time professional who works Monday to
     * Friday. This is the "sensible default" suggested by the Part 1 brief.
     */
    protected HealthProfessional(String id, String name) {
        this(id, name, DEFAULT_WORKING_DAYS);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    /**
     * Returns an unmodifiable copy of the roster, so callers can inspect it but
     * cannot alter the state of this object.
     */
    public Set<DayOfWeek> getWorkingDays() {
        return Collections.unmodifiableSet(EnumSet.copyOf(workingDays));
    }

    /** True when this professional works on the given day. */
    public boolean worksOn(DayOfWeek day) {
        return day != null && workingDays.contains(day);
    }

    /** The kind of health professional, for example "General Practitioner". */
    public abstract String getRole();

    /**
     * The role-specific task this professional performs. Each subclass supplies its
     * own version, which is what allows the different types to respond in their own
     * way when they are handled through a single HealthProfessional reference.
     */
    public abstract String performDuty();

    /** A short, readable summary of the roster, for example "Mon, Wed, Fri". */
    public String getFormattedWorkingDays() {
        StringBuilder text = new StringBuilder();
        DayOfWeek[] days = DayOfWeek.values();
        for (int i = 0; i < days.length; i++) {
            if (workingDays.contains(days[i])) {
                if (text.length() > 0) {
                    text.append(", ");
                }
                text.append(SHORT_DAY_NAMES[i]);
            }
        }
        return text.toString();
    }

    /**
     * Two health professionals are considered the same person when they are of the
     * same type and carry the same ID. This is the Java way (equals/hashCode) of
     * supporting the Part 5 question about comparing professionals, and it lets
     * collections and the double-booking check compare objects by value.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        HealthProfessional that = (HealthProfessional) other;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass(), id);
    }

    /**
     * Printing the object (for example with System.out.println(doctorA)) shows all
     * of the shared details. Subclasses call super.toString() and append their own
     * information, so no detail is repeated in the source code.
     */
    @Override
    public String toString() {
        return String.format("ID: %-4s | Name: %-18s | Role: %-28s | Works: %s",
                id, name, getRole(), getFormattedWorkingDays());
    }

    private static String validateId(String id) {
        String trimmed = (id == null) ? "" : id.trim();
        if (!trimmed.matches("\\d+")) {
            throw new IllegalArgumentException(
                    "A health professional ID must contain numbers only, but was: " + id);
        }
        return trimmed;
    }

    private static String validateName(String name) {
        String trimmed = (name == null) ? "" : name.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("A health professional must have a name.");
        }
        return trimmed;
    }

    private static EnumSet<DayOfWeek> copyWorkingDays(Set<DayOfWeek> workingDays) {
        if (workingDays == null || workingDays.isEmpty()) {
            throw new IllegalArgumentException(
                    "A health professional must work on at least one day of the week.");
        }
        EnumSet<DayOfWeek> copy = EnumSet.noneOf(DayOfWeek.class);
        for (DayOfWeek day : workingDays) {
            if (day == null) {
                throw new IllegalArgumentException("Working days cannot contain null.");
            }
            copy.add(day);
        }
        return copy;
    }
}
