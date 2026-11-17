# Java HR Management System

A console-based **Human Resource Management System** developed in Java for an Advanced Programming university course.

## Features
- Employee login and role-specific menus for employees, managers, and HR staff
- Employee profiles and contact information updates
- Full-time, part-time, and contract employee models
- Payroll calculation, payroll history, and reporting
- Leave requests and approval workflows
- File-based persistence using tab-separated text files
- Automated unit tests using JUnit 5

## Technologies and concepts
- **Java 21**, **Eclipse IDE**, **JUnit 5**
- Object-oriented programming: inheritance, interfaces, polymorphism
- Design patterns and separation of concerns: facade, factory, repositories, tax strategies
- Java collections and file I/O

## Getting started (Eclipse)
1. Install **Java JDK 21** and Eclipse IDE for Java Developers.
2. In Eclipse, choose **File → Import → General → Existing Projects into Workspace**.
3. Select the extracted project directory as the root directory and import `HRSystemProject`.
4. Run `src/hrsystem/HRSystemApp.java` as a **Java Application**.
5. Keep the six `.txt` data files in the **project root**; the app reads them using relative paths.
6. For tests, run `src/hrsystem/TestJUnit.java` as a **JUnit Test** (JUnit 5 configured in `.classpath`).

### Example login
The repository contains classroom **sample data** and fictional demo credentials:

- **HR user:** `E005` / `SE005`

Sample passwords are stored as plain text for the university exercise. This is **not a production-ready authentication system**.

## Repository layout
```text
src/hrsystem/              Java application and JUnit test source files
employees_full_time.txt   Sample full-time employee records
employees_part_time.txt   Sample part-time employee records
employees_contract.txt    Sample contract employee records
departments.txt           Department records
leaves.txt                Sample leave requests
payroll.txt               Sample payroll records
.project, .classpath      Eclipse project configuration
.settings/                Java compiler settings
```

## Notes
- This is an academic project showcasing Java programming and OOP concepts, not a deployed HR platform.
- The text files are used as the application datastore and may change when the app is used; keep a backup before experimenting.
- Generated payroll CSV exports and compiled classes are intentionally excluded from source control.
