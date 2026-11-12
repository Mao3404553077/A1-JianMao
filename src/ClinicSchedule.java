import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Part 5 - the clinic's fixed daily schedule.
 *
 * All of the time-slot knowledge lives in this one class. The clinic opens at
 * 08:00, closes at 16:00 and works in 30-minute slots, but the rest of the system
 * only ever asks this class a question ("is this a valid slot?"), so changing the
 * opening time, the closing time or the slot length automatically changes the
 * bookings the system accepts. No other class needs to be edited.
 *
 * The class is final with a private constructor because it is a utility: it holds
 * constants and static helpers, and it never makes sense to create an instance.
 */
public final class ClinicSchedule {

    public static final LocalTime OPENING_TIME = LocalTime.of(8, 0);
    public static final LocalTime CLOSING_TIME = LocalTime.of(16, 0);
    public static final int SLOT_MINUTES = 30;

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    /** Built once from the constants above, then shared read-only by every caller. */
    private static final List<LocalTime> TIME_SLOTS = buildTimeSlots();

    private ClinicSchedule() {
    }

    /**
     * The bookable start times for one day, for example 08:00, 08:30 and so on up
     * to 15:30. The list is unmodifiable so callers cannot change the schedule.
     */
    public static List<LocalTime> getTimeSlots() {
        return TIME_SLOTS;
    }

    private static List<LocalTime> buildTimeSlots() {
        List<LocalTime> slots = new ArrayList<>();
        LocalTime time = OPENING_TIME;
        while (time.isBefore(CLOSING_TIME)) {
            slots.add(time);
            time = time.plusMinutes(SLOT_MINUTES);
        }
        return Collections.unmodifiableList(slots);
    }

    /** True when the time is a real start time of a clinic slot on the current schedule. */
    public static boolean isValidTimeSlot(LocalTime time) {
        if (time == null) {
            return false;
        }
        if (time.isBefore(OPENING_TIME) || !time.isBefore(CLOSING_TIME)) {
            return false;
        }
        if (time.getSecond() != 0 || time.getNano() != 0) {
            return false;
        }
        int minutesFromOpening = time.toSecondOfDay() / 60 - OPENING_TIME.toSecondOfDay() / 60;
        return minutesFromOpening % SLOT_MINUTES == 0;
    }

    /** Formats a time the way the clinic prints it, for example "09:30". */
    public static String format(LocalTime time) {
        return time.format(TIME_FORMAT);
    }

    /** A readable description of the schedule, used in warning messages. */
    public static String describe() {
        return "between " + format(OPENING_TIME) + " and " + format(CLOSING_TIME)
                + " in " + SLOT_MINUTES + "-minute slots";
    }
}
