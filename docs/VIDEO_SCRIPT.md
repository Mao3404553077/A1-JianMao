# Five-minute video script

The rubric for the video (10%) asks you to, for each of the four pillars:

1. **name** it,
2. **explain** the concept behind it in your own words,
3. **point** to the specific place(s) in your code where it is used, and
4. **explain why** you chose to use it there.

Do not read this word for word. Learn the shape of each section, then speak
naturally. Rehearse at least twice with a timer - 5:00 is the maximum.

Recording tips: 1080p screen recording, editor on the left and console output on the
right, font size at least 16-18 pt, close email/chat windows, and check the
microphone level before recording. Upload to your SCU OneDrive and share with
"anyone with the link can view".

---

## 0:00 - 0:20 Introduction

> My name is Jian Mao, student number [your student number]. This is my PROG2004 Assessment 1:
> a Java appointment system for a health service. I will show how the four pillars
> of object-oriented programming are used in my code, and why.

Show the project tree in the IDE: `AssignmentOne`, `HealthProfessional`, `Doctor`,
three subclasses, `Patient`, `Appointment`, `ClinicSchedule`, `AppointmentManager`.

## 0:20 - 1:05 Encapsulation

> The first pillar is encapsulation. Encapsulation means an object keeps its data
> private and only exposes controlled operations, so it cannot be put into an
> invalid state.

Show `HealthProfessional.java`:

* `private final String id; private final String name;`
* `private final EnumSet<DayOfWeek> workingDays;`
* the constructor calls `validateId`, `validateName`, `copyWorkingDays`
* `getWorkingDays()` returns `Collections.unmodifiableSet(EnumSet.copyOf(...))`

> My fields are private and final, so nobody can change a professional's ID or
> roster after it is created. All validation happens once in the constructor: an ID
> must be digits only and a name cannot be blank, otherwise it throws an exception.
> I also return a copy of the working days, so outside code can read them but cannot
> modify my object. I used this because the Part 1 brief asks that external code can
> read the data but never put an object into an invalid state.

## 1:05 - 1:50 Inheritance

> The second pillar is inheritance. Inheritance lets a subclass reuse the fields and
> methods of a parent class instead of copying them.

Show `Doctor.java` and `GeneralPractitioner.java`:

* `public abstract class Doctor extends HealthProfessional`
* `public class GeneralPractitioner extends Doctor`
* `super(id, name, workingDays, practitionerNumber)`
* `toString()` calling `super.toString()`

> I have three levels: HealthProfessional, then Doctor, then GeneralPractitioner and
> Specialist. A GP is a doctor and a doctor is a health professional, so the shared
> code - the ID, the name, the roster, the toString format - is written once in
> HealthProfessional. Doctor adds the registration number and the prescribe method,
> and a GP adds only whether it accepts new patients. Each subclass calls super(...)
> first, so the shared state is always set up before its own field. I used
> inheritance here because the brief asks for specialisation without duplicating the
> common information; this way adding another type is one small class.

## 1:50 - 2:45 Polymorphism

> The third pillar is polymorphism. Polymorphism means one reference type can refer
> to different concrete types, and the object's own version of a method runs.

Show `AssignmentOne.java` Part 3 loop:

* `List<HealthProfessional> professionals`
* the loop calling `professional.performDuty()`
* the `toString()` output showing GP, Specialist, Nurse, Dietitian details

> I store all my professionals in one List of HealthProfessional and print them in
> one loop. I never write a separate loop for GPs and another for nurses. When the
> loop calls performDuty(), each object runs its own override: the GP prescribes,
> the nurse takes a blood sample. The same thing happens in Appointment: the field
> is a HealthProfessional, so one appointment class can hold any type, and its
> toString prints the correct role. I used polymorphism because the brief asks
> whether the different types can be processed together instead of separately, and
> this is exactly what makes the system easy to extend.

## 2:45 - 3:25 Abstraction

> The fourth pillar is abstraction. Abstraction means exposing what something does
> and hiding how it does it, usually with an abstract class or an interface.

Show:

* `public abstract class HealthProfessional`
* `public abstract String getRole();`
* `public abstract String performDuty();`
* maybe `ClinicSchedule` with its private constructor

> HealthProfessional is abstract, so the program cannot create a "plain" health
> professional with no specialty - which makes no sense in a real clinic. Every
> concrete type is forced to provide getRole() and performDuty(), so the rest of the
> code can rely on those methods existing. ClinicSchedule is another example: it
> hides the 08:00-16:00 slot logic behind isValidTimeSlot(), and the appointment
> manager never needs to know how the slots are built. I used abstraction because it
> makes the design safer: the compiler stops me from forgetting a method, and the
> invalid object can never exist.

## 3:25 - 4:20 Demonstration

Run `AssignmentOne` and walk through the output:

> Here is the run. Part 3 prints all eight professionals and each duty. Part 4 prints
> an appointment that links a patient, a GP and a time. Then I book seven
> appointments across all four types. I filter by professional ID 1001, then by
> mobile number, then sort the filtered subset and the whole collection by time.
> Here I try to double-book Dr Nguyen at 09:00 and the system rejects it with a
> warning; I also try 08:15, which is not a slot, and a locum who does not work
> today. Then I cancel an appointment by professional ID and time, and finally all
> appointments for one patient by mobile number, and the collection updates each
> time.

## 4:20 - 4:55 Design choices

> Some choices worth explaining. I used an ArrayList for the appointments because it
> keeps booking order and allows the same time for different professionals; a
> HashMap was not needed because an appointment is already unique by professional
> and time. I stored the time as a LocalTime, not a String, so sorting is correct
> without parsing. I used an EnumSet for the working days because the days of the
> week are a fixed set of enum values. I also made the schedule a single class so
> the clinic can change its hours in one place.

## 4:55 - 5:00 Close

> That is my appointment system and how it uses encapsulation, inheritance,
> polymorphism and abstraction. Thank you for watching.

---

## Checklist before uploading

- [ ] Video is 5:00 or less.
- [ ] All four pillars are named, explained, located in the code and justified.
- [ ] The console output is visible while the code is explained.
- [ ] Audio is clear and the screen text is readable.
- [ ] Uploaded to SCU OneDrive with a share link that the tutor can open
      (test it in a private browser window).
- [ ] Link added to the submission comments.
