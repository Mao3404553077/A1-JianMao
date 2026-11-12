# Defence questions and answers

The brief warns that you may be asked to explain your code, and that failing to do
so triggers an academic misconduct allegation. These are the questions most likely
to come up. Answer in your own words - do not memorise this file.

## Design

**1. Why is `HealthProfessional` abstract?**
Because a health professional with no specialty does not exist in the clinic. Making
the class abstract stops anyone creating a meaningless object, and it forces every
concrete type to implement `getRole()` and `performDuty()`, so the rest of the code
can rely on those methods.

**2. Why an `EnumSet<DayOfWeek>` for the roster instead of an array or a `String`?**
The days are a fixed set of values, which is exactly what an enum models. `EnumSet`
is compact, has no duplicates, and supports `contains` for the working-day check.
A `String` would need parsing and would allow "Monday, Funday".

**3. Why are the fields `final` and there are no setters?**
So an object cannot change after it is constructed. All validation happens once, in
the constructor, and the object can never be put into an invalid state. If a roster
really had to change, it should be a deliberate operation on a new object, not a
silent mutation.

**4. Why does `getWorkingDays()` return a copy?**
Returning the internal `EnumSet` would let external code call `clear()` on it and
change the object's state without validation. The unmodifiable copy keeps the
encapsulation intact.

**5. Why is there a `Doctor` class between `HealthProfessional` and the GP?**
Both a GP and a specialist prescribe and both have a registration number. Putting
that in `Doctor` means it is written once instead of twice, and a nurse does not
inherit prescribing behaviour that makes no sense for a nurse.

**6. Why is `Patient` a separate class rather than two fields in `Appointment`?**
One patient can have several appointments. A class avoids duplicating the name and
number, keeps validation in one place, and lets the filtering ask the patient object
whether a number matches.

**7. Why does `Appointment` store a `HealthProfessional` and not a `Doctor`?**
Because an appointment must work with every type in the system, including nurses and
dietitians. Storing the supertype is what makes one appointment class enough, and it
is the polymorphism the brief asks about.

**8. Why `Comparable` instead of a `Comparator`?**
An appointment has one natural order - by time - so the class itself should define
it. `Comparator` is better when several different orders are needed at different
times. With `Comparable`, `Collections.sort` works directly.

**9. Why store the time as `LocalTime` and not a `String`?**
`LocalTime` compares chronologically and formats without parsing. Sorting strings
would work by accident for "HH:mm" but not for other layouts, and validating a slot
would mean parsing.

**10. Why an `ArrayList` for the appointments?**
It keeps booking order, allows two appointments at the same time for different
professionals, and supports indexed removal when cancelling. A `HashSet` would lose
the order and a `HashMap` is unnecessary because an appointment is already unique by
professional and time.

**11. How exactly is double-booking prevented?**
`Appointment.clashesWith` checks that the time is equal and that
`professional.equals(other.professional)` is true. `equals` is based on the class and
the ID, so two objects representing the same professional compare equal even if they
are different references. `addAppointment` scans the list and returns a warning
before anything is added.

**12. What does `equals` mean for `HealthProfessional`, and why?**
Same concrete class and same ID. The ID is the unique identity of a professional, so
comparing every attribute would be wrong - two objects can legitimately differ in a
field and still be the same person. `hashCode` uses the same two values so the
equals/hashCode contract holds.

**13. Why does `addAppointment` return a `String` instead of printing?**
The brief asks for a warning message to be returned when an action cannot be
performed. Returning it keeps the manager reusable - the caller decides whether to
print it, log it or show it in a UI - and it makes the method easy to test.

**14. Why is `ClinicSchedule` a separate class?**
So the 08:00, 16:00 and 30-minute values exist in one place. If the clinic changes
its hours, only those constants change and every validation, booking and display
follows, because no other class hard-codes a time.

**15. Why sort a copy instead of sorting the manager's list?**
Sorting the internal list would change the "order booked" view and would mutate
state as a side effect of a read operation. Sorting a copy returns a view without
changing the stored collection.

## Behaviour and edge cases

**16. What happens if I try to book 08:15?**
`ClinicSchedule.isValidTimeSlot` returns false - the time is inside opening hours but
not on a 30-minute boundary - so `addAppointment` returns a warning and nothing is
added.

**17. What happens if I book the same professional at 09:00 twice for different
patients?**
The second booking is rejected with a warning naming the professional and the
existing patient. The first appointment is untouched.

**18. What happens if two different professionals are booked at 09:00?**
That is allowed. The clash test requires the same professional as well as the same
time.

**19. What happens if I cancel an appointment that does not exist?**
`cancelAppointment` returns a `Warning:` message and the collection is unchanged. The
same applies to cancelling by an unknown mobile number.

**20. How would you add a physiotherapist?**
Create `Physiotherapist extends HealthProfessional`, implement `getRole()` and
`performDuty()`, add its own field and constructor calling `super(...)`. Nothing in
`Appointment`, `AppointmentManager` or the printing loops changes, because they all
use `HealthProfessional`.

**21. What would you add if the clinic needed appointments on future days?**
Add a `LocalDate` to `Appointment`, include it in `compareTo`, in `equals` and in the
clash test, and replace the "works today" check with "works on the appointment's
day". The rest of the design already separates the date decision from the slot logic.

## Honest limitations

**22. What does your system not do?**
No user interface, no persistence, no login - the brief says not to build them. It
also does not prevent one patient being booked with two professionals at the same
time, and it has no waiting list or cancellation history, because none of those are
required.

**23. If you had more time, what would you change?**
Add a unit test class for the validation and clash logic, and replace the linear scan
in `addAppointment` with an index by professional and time if the number of
appointments grew large. Neither is needed at this size.
