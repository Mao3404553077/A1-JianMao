# Requirements trace

Every requirement in the assessment brief, where it is implemented, and how it can
be seen in the output. Useful when recording the video, when writing the report, and
if the unit assessor asks "where is this requirement met?".

## A. Assessment-wide requirements (brief, page 2 and page 8)

| Requirement | Evidence | Status |
| --- | --- | --- |
| Use the Java language | `src/*.java`, no other language, no external libraries | Done |
| Java project with all source files, zipped as `A1-username.zip` | `A1-jmao13.zip`, `run.bat`, `README.md` | Done (rename to your username) |
| WakaTime tracking | Not in this repository - install the plugin and work in the IDE | **Student action** |
| Regular GitHub commits over the assessment period | `docs/GIT_PLAN.md` gives the commit sequence | **Student action** |
| Short video on the four pillars (max 5 minutes) | `docs/VIDEO_SCRIPT.md` gives the full script | **Student action (record)** |
| Only concepts taught in Modules 1-3 | Classes, inheritance, abstract classes, encapsulation, `ArrayList`, `EnumSet`, `Comparable`, `equals`/`hashCode`, `LocalTime`, exceptions. No streams, lambdas, generics beyond the collections used, frameworks or databases | Done |
| Correct coding style with inline comments | 4-space indent, one class per file, Javadoc class comments, inline comments on design decisions | Done |
| GenAI declaration | `docs/SUBMISSION_CHECKLIST.md` contains both permitted statements | **Student action** |

## B. Part 1 - modelling a health professional

| Requirement | Where | How it is shown |
| --- | --- | --- |
| Class representing a generic health professional | `HealthProfessional` | Abstract base class of the hierarchy |
| ID consisting of numbers only | `HealthProfessional.validateId` | `matches("\\d+")`, otherwise `IllegalArgumentException` |
| Has a name | `HealthProfessional.name` + `validateName` | Blank or null name rejected |
| Fixed set of working days each week | `HealthProfessional.workingDays` (`EnumSet<DayOfWeek>`) | Printed as `Works: Mon, Tue, Wed, Thu, Fri` |
| Controlled access, object can never be invalid | private final fields, validation in the constructor, no setters, `getWorkingDays()` returns an unmodifiable copy | Part 3 output, and a run that passes an invalid ID throws |
| Readable when printed with `System.out.println` | `HealthProfessional.toString()` | Part 3 prints each professional |
| More than one way of constructing | two constructors: explicit roster, and Monday-Friday default | `new GeneralPractitioner("1001", "Dr Alice Nguyen", "MED-1001")` uses the default |

## C. Part 2 - specialising health professionals

| Requirement | Where | How it is shown |
| --- | --- | --- |
| At least four specific types, one must be a GP | `GeneralPractitioner`, `Specialist`, `Nurse`, `Dietitian` | Eight objects of four concrete types in Part 3 |
| Each type builds on Part 1 | `extends HealthProfessional` (directly or through `Doctor`) | `super(...)` in every constructor |
| No duplication of shared information | shared state lives only in `HealthProfessional`, shared doctor behaviour only in `Doctor` | `toString()` calls `super.toString()` |
| Each type presents its own details and kind | `getRole()` overridden per type; subclass `toString()` appends its own fields | Output shows GP, Specialist (Cardiology), Nurse, Dietitian |
| Shared and specific information set correctly | `super(...)` runs before the subclass field is assigned and validated | Constructors of `GeneralPractitioner`, `Specialist`, `Nurse`, `Dietitian` |
| Behaviour through the same general type | `performDuty()` abstract in the base, overridden in every type | Part 3 loop calls it through `HealthProfessional` |
| No "plain" professional; force behaviour | `abstract class HealthProfessional`, `abstract getRole()`, `abstract performDuty()` | Compiler enforces it; the class cannot be instantiated |

## D. Part 3 - working with health professional objects

| Requirement | Where | How it is shown |
| --- | --- | --- |
| Create several objects of different types (3 GPs, 1-2 of each other type) | `AssignmentOne.createHealthProfessionals` | 3 GPs, 2 specialists, 2 nurses, 1 dietitian |
| Display full details of each | Part 3 loop | 8 detailed blocks |
| Output formatted for readability | `section`, `step`, aligned `toString` | Column-aligned output |
| Avoid repetition | one `List<HealthProfessional>`, one loop | No per-type loop |
| Store and process together | `List<HealthProfessional> professionals` | Printing, duty loop, appointment booking all use the supertype |
| Adding another type in future | one new subclass, no change to the loop or appointment code | Stated in the output and in `README.md` |
| Each object responds in its own way | `performDuty()` dynamic dispatch | GP prescribes, nurse takes blood, dietitian plans diets |

## E. Part 4 - modelling an appointment

| Requirement | Where | How it is shown |
| --- | --- | --- |
| Class for a single appointment | `Appointment` | — |
| Records the patient (name and mobile) | `Patient` (name, mobile), held by `Appointment` | `Alice Wong (0412 345 678)` |
| Records when and with whom | `LocalTime time`, `HealthProfessional professional` | `09:00 | ... | General Practitioner Dr Alice Nguyen (ID 1001)` |
| Works with any professional type | field type is `HealthProfessional` | Part 4 prints an appointment with a GP and one with a nurse |
| Cannot be created incomplete or invalid | `Appointment` constructor rejects nulls and non-slot times | `new Appointment(...)` throws for invalid input |
| Displays its details including the professional | `Appointment.toString()` | Part 4 and Part 5 output |
| Can be ordered so a collection sorts by time | `implements Comparable<Appointment>`, `compareTo` | `first.compareTo(second) = -1`; sorted views in Part 5 |
| Time stored so ordering is correct | `LocalTime`, not `String` | Sorting output is chronological |

## F. Part 5 - managing the collection of appointments

| Requirement | Where | How it is shown |
| --- | --- | --- |
| Add an appointment, checking all required information | `AppointmentManager.addAppointment` | 7 confirmations, then a warning for a null patient |
| Reject double-booking of the same professional and slot | `Appointment.clashesWith` + `HealthProfessional.equals` | `Warning: Dr Alice Nguyen (ID 1001) is already booked at 09:00 ...` |
| Clinic schedule 08:00-16:00 in 30-minute slots | `ClinicSchedule.OPENING_TIME`, `CLOSING_TIME`, `SLOT_MINUTES` | Schedule line lists 16 start times |
| Reject times outside the schedule | `ClinicSchedule.isValidTimeSlot` | `Warning: 08:15 ...`, `Warning: 18:00 ...` |
| View and filter by professional ID | `findByProfessionalId` | Filtered list for ID 1001 |
| View and filter by patient phone number | `findByPatientMobileNumber` + `Patient.matchesMobileNumber` | Filtered list for 0423 456 789 |
| Sort by time, all or a filtered subset | `getAllAppointmentsSortedByTime`, `sortByTime` | Two sorted views |
| Cancel by professional ID and time | `cancelAppointment` | Cancelled message, list shrinks from 7 to 6 |
| Cancel by patient phone number | `cancelAppointmentsForPatient` | 2 removed, list shrinks from 6 to 4 |
| Warning message returned when an action cannot be performed | every method returns `String`, failures start with `Warning:` | 7 warnings in Part 6 |
| Appropriate data structure, justified | `ArrayList<Appointment>` | `README.md` section 4, Part 5 |
| Tell whether two appointments are for the same professional | `clashesWith` compares time and `professional.equals` | Double-booking rejection |
| Java way of comparing professionals | `equals`/`hashCode` by class and ID | `HealthProfessional` |
| Display appointments earliest to latest | `Collections.sort` on a copy | Sorted list, 08:00 first |
| Schedule flexible if it changes | one class holds all schedule knowledge | Changing constants changes the whole system |

## G. Part 6 - working with appointments

| Requirement | Where | How it is shown |
| --- | --- | --- |
| Add several appointments across the different types | Part 5 booking step | 7 bookings, all four types |
| Display all / filtered / sorted appointments | Part 5 | 4 list views |
| Cancel one, then display the updated collection | Part 6 | 7 -> 6 |
| Show an operation that cannot be completed | Part 6 | Double booking, invalid slot, outside hours, wrong day, null patient, missing cancellation |
| Show filtering and cancelling with different input arguments | Part 6 | Filter by ID, filter by mobile; cancel by ID+time, cancel by mobile |
| Clear output formatting | `section`, `step`, numbered rows | — |

## H. Rubric coverage (brief, pages 9-11)

| Criterion (weight) | Main evidence |
| --- | --- |
| Modelling a health professional (10%) | `HealthProfessional.java` |
| Specialising health professionals (20%) | `Doctor`, `GeneralPractitioner`, `Specialist`, `Nurse`, `Dietitian` |
| Working with health professional objects (10%) | `AssignmentOne.demonstratePartThree` |
| Modelling an appointment (15%) | `Patient.java`, `Appointment.java` |
| Managing the collection of appointments (15%) | `AppointmentManager.java`, `ClinicSchedule.java` |
| Working with appointments (10%) | `AssignmentOne.demonstratePartsFiveAndSix`, `sample-output.txt` |
| Explaining the four pillars in a video (10%) | `docs/VIDEO_SCRIPT.md` - student records |
| Version control, WakaTime, code quality (10%) | `README.md`, comments, `docs/GIT_PLAN.md`, WakaTime report |

## I. Not covered, by design

* No user interface, menus, login or persistence - the brief says not to build them.
* No check that a patient is free at the same time - not required.
* No waiting list or recurring bookings - not required.
