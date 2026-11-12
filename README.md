# PROG2004 Assessment 1 - Health Appointment System

| Item | Detail |
| --- | --- |
| Unit | PROG2004 Object Oriented Programming |
| Assessment | Assessment 1 (40%, individual) |
| Student | Jian Mao |
| Due | Monday of Week 4, 16 Nov 2026, 11:59 pm |
| Language | Java (no external libraries) |

## 1. What the program does

The program is a scripted console simulation of the back end of a health-service
appointment system. It does not have a user interface, menus, login or data
persistence - the brief explicitly says not to build those. Instead, `main` creates
health professionals of several types, prints their details, models an appointment,
books and manages a collection of appointments, and prints the results of every
operation, including the operations that must fail.

## 2. Project structure

```
A1-JianMao/
├── src/
│   ├── AssignmentOne.java         entry point and the Parts 3, 4 and 6 demonstration
│   ├── HealthProfessional.java    Part 1 - abstract base class
│   ├── Doctor.java                Part 2 - abstract class for doctors (prescribing)
│   ├── GeneralPractitioner.java   Part 2 - required concrete type
│   ├── Specialist.java            Part 2 - concrete type with a specialty
│   ├── Nurse.java                 Part 2 - concrete type that takes blood samples
│   ├── Dietitian.java             Part 2 - fourth concrete type
│   ├── Patient.java               Part 4 - patient name and mobile number
│   ├── Appointment.java           Part 4 - patient + professional + time, Comparable
│   ├── ClinicSchedule.java        Part 5 - the fixed 08:00-16:00 daily schedule
│   └── AppointmentManager.java    Part 5 - booking, filtering, sorting, cancelling
├── docs/
│   ├── VIDEO_SCRIPT.md            five-minute video plan, mapped to the code
│   ├── SUBMISSION_CHECKLIST.md    what to submit and where
│   ├── REQUIREMENTS_TRACE.md      every brief requirement and where it is met
│   ├── DEFENCE_QA.md              likely questions about the code, with answers
│   ├── GIT_PLAN.md                commit plan and commands
│   ├── WAKATIME.md                WakaTime setup and report instructions (Chinese)
│   └── TASKS_CN.md                Chinese task breakdown
├── scripts/
│   ├── Find-Jdk.ps1               locates a JDK on this machine
│   ├── build.ps1                  compiles src into out
│   └── run.ps1                    compiles, then runs AssignmentOne
├── .vscode/
│   ├── settings.json              tells VS Code that src is the source folder
│   ├── tasks.json                 "Java: build" and "Java: run" tasks (default build)
│   ├── launch.json                "Run AssignmentOne" launch configuration
│   └── extensions.json            recommends the Extension Pack for Java
├── sample-output.txt              the output of a full run
├── run.bat                        double-click build and run on Windows
└── README.md
```

The classes are in the default package because the brief asks for `AssignmentOne`
directly inside `src`. `.vscode/` and `scripts/` are development helpers, not part of
the assessed design.

## 3. Build and run

Requirements: JDK 11 or later (JDK 17 or later recommended). No third-party
libraries, build tools or databases.

### VS Code (recommended)

1. Open this folder: **File > Open Folder**.
2. Accept the recommended extensions when VS Code prompts (Extension Pack for Java;
   WakaTime is recommended too, for the time tracking the brief requires).
3. **Terminal > Run Task...** then `Java: run` to compile and run, or `Java: build`
   to compile only. The scripts find a JDK automatically (`JAVA_HOME`, the PATH, or
   a JDK installed with another application), so no PATH setup is needed.
4. With the Java extension installed, **F5** also runs the `Run AssignmentOne`
   configuration in `.vscode/launch.json`.

### Command line

PowerShell, from the project folder:

```
javac -encoding UTF-8 -d out (Get-ChildItem src\*.java).FullName
java -cp out AssignmentOne
```

or simply:

```
.\run.bat
```

`javac src\*.java` does not work on Windows: cmd does not expand the wildcard, so
javac receives a literal `*.java` path. The scripts and the IDE extensions build the
file list for you.

IntelliJ IDEA: open the `A1-JianMao` folder, mark `src` as **Sources Root**, then run
`AssignmentOne`. Eclipse: create a Java project, point it at `src`, run
`AssignmentOne`.

## 4. Design

### Class relationships

```
                    HealthProfessional (abstract)
                    id, name, workingDays
                    + getRole(), performDuty()   (abstract)
                            ▲
          ┌─────────────────┼──────────────────┬─────────────────┐
      Doctor (abstract)   Nurse            Dietitian
   practitionerNumber  assignedWard     dietaryFocus
          ▲
   ┌──────┴───────┐
GeneralPractitioner  Specialist
 acceptingNewPatients specialty
```

```
Appointment  ◆── Patient
             ◆── HealthProfessional   (any concrete type)
AppointmentManager  ◆── List<Appointment>
ClinicSchedule      (static utility: the fixed daily slots)
```

`◆` means "has a": an appointment has a patient and a health professional, and the
manager has a collection of appointments. `▲` means "is a": every subclass is a
`HealthProfessional`.

### Part 1 - modelling a health professional

* `id`, `name` and `workingDays` are `private final`, so an object cannot be edited
  after construction. The ID must be digits only and the name cannot be blank;
  invalid values throw `IllegalArgumentException` from the constructor, so an object
  can never exist in an invalid state.
* The roster is an `EnumSet<DayOfWeek>`: a fixed collection of enum values, which is
  exactly what "works on a fixed set of days each week" describes. The set is copied
  on the way in and wrapped as unmodifiable on the way out (`getWorkingDays`), so
  callers can read it but cannot change the object's state.
* Two constructors are provided: one takes an explicit roster, the other defaults to
  Monday to Friday. That is the "sensible default" the brief asks about.
* `toString` prints the ID, name, role and roster, so `System.out.println(doctorA)`
  displays readable details.
* `equals` and `hashCode` use the ID together with the concrete class, so two
  professionals are the same professional when they are the same type with the same
  ID. This is what the double-booking check uses in Part 5.

### Part 2 - specialising health professionals

* `GeneralPractitioner`, `Specialist`, `Nurse` and `Dietitian` are four distinct
  concrete types; `Doctor` is an extra abstract layer between
  `HealthProfessional` and the two doctor types, which shows inheritance that is
  more than one level deep and lets both doctor types share `prescribe`.
* Constructors call `super(...)` first, so the shared state is always set up before
  the type-specific state. Each subclass validates only its own additional field.
* `getRole()` and `performDuty()` are overridden by every concrete type, so each type
  presents its own details and its own task.
* `toString()` in every subclass appends to `super.toString()`, so the shared
  details are printed once and the type-specific details are added, without copying
  the format string into every class.
* `HealthProfessional` is abstract: a "plain" health professional with no specialty
  cannot be created, and every concrete type is forced to provide `getRole()` and
  `performDuty()`.

### Part 3 - working with health professional objects

* All eight objects are stored in a single `List<HealthProfessional>` and printed
  in one loop. Adding a fifth type would need one new class and no change to the
  loop, the list or the appointment code.
* The loop that calls `performDuty()` shows each object responding in its own way
  through the common type. `Doctor.prescribe` and `Nurse.takeBloodSample` show
  behaviour that only a specific type has.

### Part 4 - modelling an appointment

* `Patient` is its own class: an appointment only needs to know about a patient, and
  the same patient can book more than one appointment without duplicating details.
* `Appointment` holds a `Patient`, a `HealthProfessional` and a `LocalTime`. Because
  the field is the supertype, one appointment class works with every professional
  type.
* The constructor rejects nulls and times that are not clinic slots, so an
  appointment cannot be created in an incomplete or invalid state.
* `LocalTime` is used instead of a `String` because it compares and sorts correctly
  without parsing. `Appointment implements Comparable<Appointment>` and orders by
  time, using the professional ID to break a tie.
* `toString` shows the time, the patient and the chosen professional, including the
  professional's role.

### Part 5 - managing the collection of appointments

* The collection is an `ArrayList<Appointment>`. It keeps booking order, allows two
  appointments in the same slot for different professionals, and supports every
  required operation. A `HashSet` would lose the order needed for the time-ordered
  view; a `HashMap` is unnecessary because appointments are already unique by
  professional and time.
* `addAppointment` checks, in order: all three values present, the time is a real
  slot, the professional works today, and the professional is not already booked.
  Each failure returns a warning `String`; success returns a confirmation `String`.
* Double-booking is detected with `Appointment.clashesWith`, which compares the time
  and uses `HealthProfessional.equals`.
* Filtering is available by professional ID and by patient mobile number. Sorting
  works on a copy (`new ArrayList<>(source)` then `Collections.sort`), so sorting a
  filtered subset never reorders the manager's own list.
* Cancelling works by professional ID and time, or by mobile number, and returns a
  warning when nothing matches.
* `ClinicSchedule` owns the opening time, closing time and slot length. Every other
  class asks it whether a time is valid, so changing the schedule in one place
  changes the behaviour of the whole system.

### Part 6 - working with appointments

`main` demonstrates, in order: booking across all four types, viewing all
appointments, filtering by professional ID, filtering by mobile number, sorting all
appointments and a filtered subset, rejecting a double booking, rejecting a time
that is not a slot, rejecting a professional who does not work today, cancelling by
professional ID and time, cancelling by mobile number, and displaying the updated
collection after each change.

## 5. The four pillars of OOP in this code

| Pillar | Where it is used | Why |
| --- | --- | --- |
| Encapsulation | `HealthProfessional` (private final fields, validation, `getWorkingDays` copy), `Patient`, `Appointment` | Objects protect their own data and can never be put into an invalid state; validation happens once, in the constructor. |
| Inheritance | `Doctor extends HealthProfessional`, `GeneralPractitioner extends Doctor`, `Nurse`/`Dietitian extends HealthProfessional` | Shared state and behaviour (`id`, `name`, roster, `toString`, `equals`) are written once, and each subclass adds only what is different. |
| Polymorphism | `List<HealthProfessional>` in `AssignmentOne`, `getRole()`/`performDuty()` overrides, `Appointment` holding a `HealthProfessional`, `Comparable` | One loop or one field can work with every professional type, and each object still behaves according to its real type at run time. |
| Abstraction | `abstract class HealthProfessional`, `abstract class Doctor`, `abstract getRole()`/`performDuty()` | Callers depend on what a health professional can do, not on how each type does it, and a meaningless "plain" professional cannot be instantiated. |

## 6. Assumptions

* Bookings are only for the current day, so the appointment stores a time but no
  date. The working-day check uses `LocalDate.now()`.
* The clinic runs 08:00-16:00 in 30-minute slots, so the last bookable start time is
  15:30. This is set in `ClinicSchedule` and can be changed there.
* A mobile number is an Australian mobile: ten digits starting with `04`. Spaces,
  dashes and a `+61` prefix are accepted and normalised.
* For the demonstration to work on any day, the created professionals are rostered
  Monday to Friday plus today. A separate locum who does not work today is used to
  demonstrate the working-day rejection.
* There is no persistence: the system keeps everything in memory, as the brief
  requires.

## 7. Limitations

* Two patients can still book the same professional at different times, as intended;
  the system does not model patient availability.
* There is no waiting list, no recurring appointments and no cancellation history,
  because none of these are required by the brief.
