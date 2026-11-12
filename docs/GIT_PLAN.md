# Git plan

The rubric gives 10% to professional practice: "frequent, meaningful commits that
clearly evidence incremental progress", a WakaTime report, and clean code. One
giant commit at the end scores badly, and a fabricated history is easy to spot
because commit dates and WakaTime will not match.

Plan: commit once per meaningful step, on the day you actually do the work. With
four days left before the deadline, the sequence below is realistic.

## Day 1 - modelling (Parts 1 and 2)

```
git init
git add .gitignore README.md
git commit -m "Add project skeleton and README"

git add src/HealthProfessional.java
git commit -m "Add abstract HealthProfessional with validated ID, name and roster"

git add src/Doctor.java src/GeneralPractitioner.java src/Specialist.java
git commit -m "Add Doctor layer with GP and Specialist subclasses"

git add src/Nurse.java src/Dietitian.java
git commit -m "Add Nurse and Dietitian types with their own duties"
```

## Day 2 - appointments (Part 4)

```
git add src/Patient.java
git commit -m "Add Patient class with validated Australian mobile number"

git add src/Appointment.java src/ClinicSchedule.java
git commit -m "Add Appointment (Comparable, validated) and fixed daily schedule"
```

## Day 3 - management (Part 5)

```
git add src/AppointmentManager.java
git commit -m "Add AppointmentManager: booking, filtering, sorting and cancellation"

git add src/AssignmentOne.java
git commit -m "Add AssignmentOne demonstration for Parts 3, 4 and 6"

git add sample-output.txt
git commit -m "Add sample console output of a full run"
```

## Day 4 - polish (before submitting)

```
git add -A
git commit -m "Review comments, formatting and README before submission"
git push -u origin main
```

## Commands

```powershell
git status
git add -A
git commit -m "Describe the change in the imperative mood"
git push
```

This repository already exists and is connected:

```
https://github.com/Mao3404553077/A1-jmao13        (private)
origin  git@github.com:Mao3404553077/A1-jmao13.git
```

The remote uses SSH because `github.com:443` is blocked on this network, while
port 22 is open. Git for Windows cannot read a user profile path that contains
Chinese characters, so this repository tells git to use the Windows OpenSSH
client:

```powershell
git config core.sshCommand '"C:/Windows/System32/OpenSSH/ssh.exe" -o StrictHostKeyChecking=accept-new'
```

A deploy key for this one repository is installed on the GitHub side, so pushing
does not ask for a password. Add the tutor and the unit assessor under
**Settings > Collaborators** before the deadline.

Compile before every commit so no commit is broken:

```powershell
.\scripts\build.ps1
.\scripts\run.ps1
```

## Rules of thumb

* One commit = one coherent change. "Fix typo in warning message" is a fine commit;
  "stuff" is not.
* Write the message in the imperative mood: "Add ...", "Fix ...", "Refactor ...".
* Do not commit `out/` or `.class` files.
* Push at least once a day so the history is visible on GitHub, not only locally.
* If the repository is private, invite the tutor and the unit assessor as
  collaborators before the deadline.
* Never rewrite dates or fabricate history. The commit timestamps should match when
  WakaTime recorded your work.
