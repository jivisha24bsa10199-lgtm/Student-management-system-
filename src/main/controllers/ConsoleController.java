package com.sms.controllers;

import com.sms.exceptions.DatabaseException;
import com.sms.exceptions.RecordNotFoundException;
import com.sms.exceptions.ValidationException;
import com.sms.models.*;
import com.sms.services.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class ConsoleController {
    private final StudentService studentService;
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;
    private final AttendanceService attendanceService;
    private final Scanner scanner;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ConsoleController() {
        this.studentService = new StudentService();
        this.courseService = new CourseService();
        this.enrollmentService = new EnrollmentService();
        this.attendanceService = new AttendanceService();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("==============================================");
        System.out.println("  Student Management System");
        System.out.println("==============================================\n");

        boolean running = true;
        while (running) {
            displayMainMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    handleStudentMenu();
                    break;
                case "2":
                    handleCourseMenu();
                    break;
                case "3":
                    handleEnrollmentMenu();
                    break;
                case "4":
                    handleAttendanceMenu();
                    break;
                case "5":
                    handleReportsMenu();
                    break;
                case "0":
                    running = false;
                    System.out.println("\nThank you for using Student Management System!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void displayMainMenu() {
        System.out.println("\n========== MAIN MENU ==========");
        System.out.println("1. Student Management");
        System.out.println("2. Course Management");
        System.out.println("3. Enrollment Management");
        System.out.println("4. Attendance Management");
        System.out.println("5. Reports & Analytics");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    private void handleStudentMenu() {
        while (true) {
            System.out.println("\n===== STUDENT MANAGEMENT =====");
            System.out.println("1. Add New Student");
            System.out.println("2. View All Students");
            System.out.println("3. Search Student");
            System.out.println("4. Update Student");
            System.out.println("5. Delete Student");
            System.out.println("6. Change Student Status");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1":
                        addStudent();
                        break;
                    case "2":
                        viewAllStudents();
                        break;
                    case "3":
                        searchStudent();
                        break;
                    case "4":
                        updateStudent();
                        break;
                    case "5":
                        deleteStudent();
                        break;
                    case "6":
                        changeStudentStatus();
                        break;
                    case "0":
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void addStudent() throws ValidationException, DatabaseException {
        System.out.println("\n----- Add New Student -----");
        System.out.print("Student ID (e.g., STU001): ");
        String studentId = scanner.nextLine().trim();

        System.out.print("First Name: ");
        String firstName = scanner.nextLine().trim();

        System.out.print("Last Name: ");
        String lastName = scanner.nextLine().trim();

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Phone (10 digits): ");
        String phone = scanner.nextLine().trim();

        System.out.print("Date of Birth (yyyy-MM-dd): ");
        String dobStr = scanner.nextLine().trim();
        LocalDate dob = LocalDate.parse(dobStr, dateFormatter);

        System.out.print("Address: ");
        String address = scanner.nextLine().trim();

        Student student = new Student(studentId, firstName, lastName, email, phone, dob, address);
        studentService.addStudent(student);

        System.out.println("\nStudent added successfully!");
    }

    private void viewAllStudents() throws DatabaseException {
        System.out.println("\n----- All Students -----");
        List<Student> students = studentService.getAllStudents();

        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        System.out.printf("%-12s %-20s %-30s %-15s %-10s%n",
                "Student ID", "Name", "Email", "Phone", "Status");
        System.out.println("------------------------------------------------------------------------------------");

        for (Student student : students) {
            System.out.printf("%-12s %-20s %-30s %-15s %-10s%n",
                    student.getStudentId(),
                    student.getFirstName() + " " + student.getLastName(),
                    student.getEmail(),
                    student.getPhone(),
                    student.getStatus());
        }
    }

    private void searchStudent() throws DatabaseException, RecordNotFoundException {
        System.out.println("\n----- Search Student -----");
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine().trim();

        Student student = studentService.getStudentByStudentId(studentId);
        displayStudentDetails(student);
    }

    private void displayStudentDetails(Student student) {
        System.out.println("\n----- Student Details -----");
        System.out.println("ID: " + student.getStudentId());
        System.out.println("Name: " + student.getFirstName() + " " + student.getLastName());
        System.out.println("Email: " + student.getEmail());
        System.out.println("Phone: " + student.getPhone());
        System.out.println("Date of Birth: " + student.getDateOfBirth());
        System.out.println("Address: " + student.getAddress());
        System.out.println("Enrollment Date: " + student.getEnrollmentDate());
        System.out.println("Status: " + student.getStatus());
    }

    private void updateStudent() throws DatabaseException, RecordNotFoundException, ValidationException {
        System.out.println("\n----- Update Student -----");
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine().trim();

        Student student = studentService.getStudentByStudentId(studentId);
        displayStudentDetails(student);

        System.out.println("\nEnter new details (press Enter to keep current value):");

        System.out.print("First Name [" + student.getFirstName() + "]: ");
        String firstName = scanner.nextLine().trim();
        if (!firstName.isEmpty()) student.setFirstName(firstName);

        System.out.print("Last Name [" + student.getLastName() + "]: ");
        String lastName = scanner.nextLine().trim();
        if (!lastName.isEmpty()) student.setLastName(lastName);

        System.out.print("Email [" + student.getEmail() + "]: ");
        String email = scanner.nextLine().trim();
        if (!email.isEmpty()) student.setEmail(email);

        System.out.print("Phone [" + student.getPhone() + "]: ");
        String phone = scanner.nextLine().trim();
        if (!phone.isEmpty()) student.setPhone(phone);

        System.out.print("Address [" + student.getAddress() + "]: ");
        String address = scanner.nextLine().trim();
        if (!address.isEmpty()) student.setAddress(address);

        studentService.updateStudent(student);
        System.out.println("\nStudent updated successfully!");
    }

    private void deleteStudent() throws DatabaseException, RecordNotFoundException {
        System.out.println("\n----- Delete Student -----");
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine().trim();

        Student student = studentService.getStudentByStudentId(studentId);
        displayStudentDetails(student);

        System.out.print("\nAre you sure you want to delete this student? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("yes")) {
            studentService.deleteStudent(student.getId());
            System.out.println("Student deleted successfully!");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private void changeStudentStatus() throws DatabaseException, RecordNotFoundException, ValidationException {
        System.out.println("\n----- Change Student Status -----");
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine().trim();

        Student student = studentService.getStudentByStudentId(studentId);
        System.out.println("Current Status: " + student.getStatus());

        System.out.println("\nSelect new status:");
        System.out.println("1. Active");
        System.out.println("2. Inactive");
        System.out.println("3. Graduated");
        System.out.print("Enter choice: ");

        String choice = scanner.nextLine().trim();
        String newStatus;

        switch (choice) {
            case "1":
                newStatus = "Active";
                break;
            case "2":
                newStatus = "Inactive";
                break;
            case "3":
                newStatus = "Graduated";
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        studentService.changeStudentStatus(student.getId(), newStatus);
        System.out.println("Student status updated successfully!");
    }

    private void handleCourseMenu() {
        while (true) {
            System.out.println("\n===== COURSE MANAGEMENT =====");
            System.out.println("1. Add New Course");
            System.out.println("2. View All Courses");
            System.out.println("3. Search Course");
            System.out.println("4. Update Course");
            System.out.println("5. Delete Course");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1":
                        addCourse();
                        break;
                    case "2":
                        viewAllCourses();
                        break;
                    case "3":
                        searchCourse();
                        break;
                    case "4":
                        updateCourse();
                        break;
                    case "5":
                        deleteCourse();
                        break;
                    case "0":
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void addCourse() throws ValidationException, DatabaseException {
        System.out.println("\n----- Add New Course -----");
        System.out.print("Course Code (e.g., CS101): ");
        String courseCode = scanner.nextLine().trim();

        System.out.print("Course Name: ");
        String courseName = scanner.nextLine().trim();

        System.out.print("Description: ");
        String description = scanner.nextLine().trim();

        System.out.print("Credits (1-6): ");
        int credits = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Instructor: ");
        String instructor = scanner.nextLine().trim();

        System.out.print("Semester (e.g., Fall 2024): ");
        String semester = scanner.nextLine().trim();

        System.out.print("Max Capacity: ");
        int maxCapacity = Integer.parseInt(scanner.nextLine().trim());

        Course course = new Course(courseCode, courseName, description, credits, instructor, semester, maxCapacity);
        courseService.addCourse(course);

        System.out.println("\nCourse added successfully!");
    }

    private void viewAllCourses() throws DatabaseException {
        System.out.println("\n----- All Courses -----");
        List<Course> courses = courseService.getAllCourses();

        if (courses.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }

        System.out.printf("%-10s %-30s %-8s %-20s %-15s %-10s%n",
                "Code", "Name", "Credits", "Instructor", "Semester", "Capacity");
        System.out.println("------------------------------------------------------------------------------------------------");

        for (Course course : courses) {
            System.out.printf("%-10s %-30s %-8d %-20s %-15s %-10d%n",
                    course.getCourseCode(),
                    course.getCourseName(),
                    course.getCredits(),
                    course.getInstructor(),
                    course.getSemester(),
                    course.getMaxCapacity());
        }
    }

    private void searchCourse() throws DatabaseException, RecordNotFoundException {
        System.out.println("\n----- Search Course -----");
        System.out.print("Enter Course Code: ");
        String courseCode = scanner.nextLine().trim();

        Course course = courseService.getCourseByCourseCode(courseCode);
        displayCourseDetails(course);
    }

    private void displayCourseDetails(Course course) throws DatabaseException {
        System.out.println("\n----- Course Details -----");
        System.out.println("Code: " + course.getCourseCode());
        System.out.println("Name: " + course.getCourseName());
        System.out.println("Description: " + course.getDescription());
        System.out.println("Credits: " + course.getCredits());
        System.out.println("Instructor: " + course.getInstructor());
        System.out.println("Semester: " + course.getSemester());
        System.out.println("Max Capacity: " + course.getMaxCapacity());

        int enrolledCount = enrollmentService.getCourseEnrollmentCount(course.getId());
        System.out.println("Currently Enrolled: " + enrolledCount);
        System.out.println("Available Seats: " + (course.getMaxCapacity() - enrolledCount));
    }

    private void updateCourse() throws DatabaseException, RecordNotFoundException, ValidationException {
        System.out.println("\n----- Update Course -----");
        System.out.print("Enter Course Code: ");
        String courseCode = scanner.nextLine().trim();

        Course course = courseService.getCourseByCourseCode(courseCode);
        displayCourseDetails(course);

        System.out.println("\nEnter new details (press Enter to keep current value):");

        System.out.print("Course Name [" + course.getCourseName() + "]: ");
        String courseName = scanner.nextLine().trim();
        if (!courseName.isEmpty()) course.setCourseName(courseName);

        System.out.print("Description [" + course.getDescription() + "]: ");
        String description = scanner.nextLine().trim();
        if (!description.isEmpty()) course.setDescription(description);

        System.out.print("Credits [" + course.getCredits() + "]: ");
        String creditsStr = scanner.nextLine().trim();
        if (!creditsStr.isEmpty()) course.setCredits(Integer.parseInt(creditsStr));

        System.out.print("Instructor [" + course.getInstructor() + "]: ");
        String instructor = scanner.nextLine().trim();
        if (!instructor.isEmpty()) course.setInstructor(instructor);

        System.out.print("Semester [" + course.getSemester() + "]: ");
        String semester = scanner.nextLine().trim();
        if (!semester.isEmpty()) course.setSemester(semester);

        courseService.updateCourse(course);
        System.out.println("\nCourse updated successfully!");
    }

    private void deleteCourse() throws DatabaseException, RecordNotFoundException {
        System.out.println("\n----- Delete Course -----");
        System.out.print("Enter Course Code: ");
        String courseCode = scanner.nextLine().trim();

        Course course = courseService.getCourseByCourseCode(courseCode);
        displayCourseDetails(course);

        System.out.print("\nAre you sure you want to delete this course? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("yes")) {
            courseService.deleteCourse(course.getId());
            System.out.println("Course deleted successfully!");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private void handleEnrollmentMenu() {
        while (true) {
            System.out.println("\n===== ENROLLMENT MANAGEMENT =====");
            System.out.println("1. Enroll Student in Course");
            System.out.println("2. View Student Enrollments");
            System.out.println("3. View Course Enrollments");
            System.out.println("4. Update Grade");
            System.out.println("5. Drop Enrollment");
            System.out.println("6. Complete Enrollment");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1":
                        enrollStudent();
                        break;
                    case "2":
                        viewStudentEnrollments();
                        break;
                    case "3":
                        viewCourseEnrollments();
                        break;
                    case "4":
                        updateGrade();
                        break;
                    case "5":
                        dropEnrollment();
                        break;
                    case "6":
                        completeEnrollment();
                        break;
                    case "0":
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void enrollStudent() throws DatabaseException, RecordNotFoundException, ValidationException {
        System.out.println("\n----- Enroll Student in Course -----");
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine().trim();

        Student student = studentService.getStudentByStudentId(studentId);
        System.out.println("Student: " + student.getFirstName() + " " + student.getLastName());

        System.out.print("Enter Course Code: ");
        String courseCode = scanner.nextLine().trim();

        Course course = courseService.getCourseByCourseCode(courseCode);
        System.out.println("Course: " + course.getCourseName());

        enrollmentService.enrollStudent(student.getId(), course.getId());
        System.out.println("\nStudent enrolled successfully!");
    }

    private void viewStudentEnrollments() throws DatabaseException, RecordNotFoundException {
        System.out.println("\n----- View Student Enrollments -----");
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine().trim();

        Student student = studentService.getStudentByStudentId(studentId);
        List<Enrollment> enrollments = enrollmentService.getStudentEnrollments(student.getId());

        if (enrollments.isEmpty()) {
            System.out.println("No enrollments found for this student.");
            return;
        }

        System.out.println("\nEnrollments for " + student.getFirstName() + " " + student.getLastName() + ":");
        System.out.printf("%-10s %-30s %-12s %-10s %-10s%n",
                "Code", "Course Name", "Status", "Grade", "Date");
        System.out.println("------------------------------------------------------------------------");

        for (Enrollment enrollment : enrollments) {
            System.out.printf("%-10s %-30s %-12s %-10s %-10s%n",
                    enrollment.getCourse().getCourseCode(),
                    enrollment.getCourse().getCourseName(),
                    enrollment.getStatus(),
                    enrollment.getGrade() != null ? enrollment.getGrade() : "N/A",
                    enrollment.getEnrollmentDate());
        }
    }

    private void viewCourseEnrollments() throws DatabaseException, RecordNotFoundException {
        System.out.println("\n----- View Course Enrollments -----");
        System.out.print("Enter Course Code: ");
        String courseCode = scanner.nextLine().trim();

        Course course = courseService.getCourseByCourseCode(courseCode);
        List<Enrollment> enrollments = enrollmentService.getCourseEnrollments(course.getId());

        if (enrollments.isEmpty()) {
            System.out.println("No enrollments found for this course.");
            return;
        }

        System.out.println("\nEnrollments for " + course.getCourseName() + ":");
        System.out.printf("%-12s %-25s %-12s %-10s%n",
                "Student ID", "Name", "Status", "Grade");
        System.out.println("----------------------------------------------------------");

        for (Enrollment enrollment : enrollments) {
            System.out.printf("%-12s %-25s %-12s %-10s%n",
                    enrollment.getStudent().getStudentId(),
                    enrollment.getStudent().getFirstName() + " " + enrollment.getStudent().getLastName(),
                    enrollment.getStatus(),
                    enrollment.getGrade() != null ? enrollment.getGrade() : "N/A");
        }
    }

    private void updateGrade() throws DatabaseException, RecordNotFoundException, ValidationException {
        System.out.println("\n----- Update Grade -----");
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine().trim();

        Student student = studentService.getStudentByStudentId(studentId);
        List<Enrollment> enrollments = enrollmentService.getStudentEnrollments(student.getId());

        if (enrollments.isEmpty()) {
            System.out.println("No enrollments found for this student.");
            return;
        }

        System.out.println("\nEnrollments:");
        for (int i = 0; i < enrollments.size(); i++) {
            Enrollment e = enrollments.get(i);
            System.out.println((i + 1) + ". " + e.getCourse().getCourseCode() + " - " +
                    e.getCourse().getCourseName() + " [Current Grade: " +
                    (e.getGrade() != null ? e.getGrade() : "N/A") + "]");
        }

        System.out.print("Select enrollment (enter number): ");
        int choice = Integer.parseInt(scanner.nextLine().trim()) - 1;

        if (choice < 0 || choice >= enrollments.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        System.out.print("Enter new grade (A+, A, A-, B+, B, B-, C+, C, C-, D, F): ");
        String grade = scanner.nextLine().trim().toUpperCase();

        enrollmentService.updateGrade(enrollments.get(choice).getId(), grade);
        System.out.println("Grade updated successfully!");
    }

    private void dropEnrollment() throws DatabaseException, RecordNotFoundException {
        System.out.println("\n----- Drop Enrollment -----");
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine().trim();

        Student student = studentService.getStudentByStudentId(studentId);
        List<Enrollment> enrollments = enrollmentService.getStudentEnrollments(student.getId());

        if (enrollments.isEmpty()) {
            System.out.println("No enrollments found for this student.");
            return;
        }

        System.out.println("\nEnrollments:");
        for (int i = 0; i < enrollments.size(); i++) {
            Enrollment e = enrollments.get(i);
            System.out.println((i + 1) + ". " + e.getCourse().getCourseCode() + " - " +
                    e.getCourse().getCourseName() + " [" + e.getStatus() + "]");
        }

        System.out.print("Select enrollment to drop (enter number): ");
        int choice = Integer.parseInt(scanner.nextLine().trim()) - 1;

        if (choice < 0 || choice >= enrollments.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        System.out.print("Are you sure? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("yes")) {
            enrollmentService.dropEnrollment(enrollments.get(choice).getId());
            System.out.println("Enrollment dropped successfully!");
        }
    }

    private void completeEnrollment() throws DatabaseException, RecordNotFoundException, ValidationException {
        System.out.println("\n----- Complete Enrollment -----");
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine().trim();

        Student student = studentService.getStudentByStudentId(studentId);
        List<Enrollment> enrollments = enrollmentService.getStudentEnrollments(student.getId());

        if (enrollments.isEmpty()) {
            System.out.println("No enrollments found for this student.");
            return;
        }

        System.out.println("\nEnrollments:");
        for (int i = 0; i < enrollments.size(); i++) {
            Enrollment e = enrollments.get(i);
            if (e.getStatus().equals("Enrolled")) {
                System.out.println((i + 1) + ". " + e.getCourse().getCourseCode() + " - " +
                        e.getCourse().getCourseName());
            }
        }

        System.out.print("Select enrollment to complete (enter number): ");
        int choice = Integer.parseInt(scanner.nextLine().trim()) - 1;

        if (choice < 0 || choice >= enrollments.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        System.out.print("Enter final grade (A+, A, A-, B+, B, B-, C+, C, C-, D, F): ");
        String grade = scanner.nextLine().trim().toUpperCase();

        enrollmentService.completeEnrollment(enrollments.get(choice).getId(), grade);
        System.out.println("Enrollment completed successfully!");
    }

    private void handleAttendanceMenu() {
        while (true) {
            System.out.println("\n===== ATTENDANCE MANAGEMENT =====");
            System.out.println("1. Mark Attendance");
            System.out.println("2. View Student Attendance");
            System.out.println("3. View Attendance Percentage");
            System.out.println("4. Update Attendance");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1":
                        markAttendance();
                        break;
                    case "2":
                        viewStudentAttendance();
                        break;
                    case "3":
                        viewAttendancePercentage();
                        break;
                    case "4":
                        updateAttendance();
                        break;
                    case "0":
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void markAttendance() throws DatabaseException, RecordNotFoundException, ValidationException {
        System.out.println("\n----- Mark Attendance -----");
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine().trim();

        Student student = studentService.getStudentByStudentId(studentId);
        List<Enrollment> enrollments = enrollmentService.getStudentEnrollments(student.getId());

        if (enrollments.isEmpty()) {
            System.out.println("No active enrollments found.");
            return;
        }

        System.out.println("\nActive Enrollments:");
        int index = 1;
        for (Enrollment e : enrollments) {
            if (e.getStatus().equals("Enrolled")) {
                System.out.println(index + ". " + e.getCourse().getCourseCode() + " - " +
                        e.getCourse().getCourseName());
                index++;
            }
        }

        System.out.print("Select enrollment (enter number): ");
        int choice = Integer.parseInt(scanner.nextLine().trim()) - 1;

        if (choice < 0 || choice >= enrollments.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        System.out.print("Date (yyyy-MM-dd) [press Enter for today]: ");
        String dateStr = scanner.nextLine().trim();
        LocalDate date = dateStr.isEmpty() ? LocalDate.now() : LocalDate.parse(dateStr, dateFormatter);

        System.out.println("Select status:");
        System.out.println("1. Present");
        System.out.println("2. Absent");
        System.out.println("3. Late");
        System.out.print("Enter choice: ");
        String statusChoice = scanner.nextLine().trim();

        String status;
        switch (statusChoice) {
            case "1":
                status = "Present";
                break;
            case "2":
                status = "Absent";
                break;
            case "3":
                status = "Late";
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        attendanceService.markAttendance(enrollments.get(choice).getId(), date, status);
        System.out.println("Attendance marked successfully!");
    }

    private void viewStudentAttendance() throws DatabaseException, RecordNotFoundException {
        System.out.println("\n----- View Student Attendance -----");
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine().trim();

        Student student = studentService.getStudentByStudentId(studentId);
        List<Enrollment> enrollments = enrollmentService.getStudentEnrollments(student.getId());

        if (enrollments.isEmpty()) {
            System.out.println("No enrollments found.");
            return;
        }

        System.out.println("\nEnrollments:");
        for (int i = 0; i < enrollments.size(); i++) {
            Enrollment e = enrollments.get(i);
            System.out.println((i + 1) + ". " + e.getCourse().getCourseCode() + " - " +
                    e.getCourse().getCourseName());
        }

        System.out.print("Select enrollment (enter number): ");
        int choice = Integer.parseInt(scanner.nextLine().trim()) - 1;

        if (choice < 0 || choice >= enrollments.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        List<Attendance> attendanceList = attendanceService.getEnrollmentAttendance(enrollments.get(choice).getId());

        if (attendanceList.isEmpty()) {
            System.out.println("No attendance records found.");
            return;
        }

        System.out.println("\nAttendance Records:");
        System.out.printf("%-15s %-10s %-20s%n", "Date", "Status", "Remarks");
        System.out.println("-----------------------------------------------");

        for (Attendance att : attendanceList) {
            System.out.printf("%-15s %-10s %-20s%n",
                    att.getAttendanceDate(),
                    att.getStatus(),
                    att.getRemarks() != null ? att.getRemarks() : "");
        }
    }

    private void viewAttendancePercentage() throws DatabaseException, RecordNotFoundException {
        System.out.println("\n----- View Attendance Percentage -----");
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine().trim();

        Student student = studentService.getStudentByStudentId(studentId);
        List<Enrollment> enrollments = enrollmentService.getStudentEnrollments(student.getId());

        if (enrollments.isEmpty()) {
            System.out.println("No enrollments found.");
            return;
        }

        System.out.println("\nAttendance Summary for " + student.getFirstName() + " " + student.getLastName() + ":");
        System.out.printf("%-10s %-30s %-15s%n", "Code", "Course Name", "Attendance %");
        System.out.println("-------------------------------------------------------");

        for (Enrollment enrollment : enrollments) {
            double percentage = attendanceService.getAttendancePercentage(enrollment.getId());
            System.out.printf("%-10s %-30s %-15.2f%%%n",
                    enrollment.getCourse().getCourseCode(),
                    enrollment.getCourse().getCourseName(),
                    percentage);
        }
    }

    private void updateAttendance() throws DatabaseException, RecordNotFoundException, ValidationException {
        System.out.println("\n----- Update Attendance -----");
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine().trim();

        Student student = studentService.getStudentByStudentId(studentId);
        List<Enrollment> enrollments = enrollmentService.getStudentEnrollments(student.getId());

        if (enrollments.isEmpty()) {
            System.out.println("No enrollments found.");
            return;
        }

        System.out.println("\nEnrollments:");
        for (int i = 0; i < enrollments.size(); i++) {
            Enrollment e = enrollments.get(i);
            System.out.println((i + 1) + ". " + e.getCourse().getCourseCode() + " - " +
                    e.getCourse().getCourseName());
        }

        System.out.print("Select enrollment (enter number): ");
        int choice = Integer.parseInt(scanner.nextLine().trim()) - 1;

        if (choice < 0 || choice >= enrollments.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        List<Attendance> attendanceList = attendanceService.getEnrollmentAttendance(enrollments.get(choice).getId());

        if (attendanceList.isEmpty()) {
            System.out.println("No attendance records found.");
            return;
        }

        System.out.println("\nAttendance Records:");
        for (int i = 0; i < attendanceList.size(); i++) {
            Attendance att = attendanceList.get(i);
            System.out.println((i + 1) + ". " + att.getAttendanceDate() + " - " + att.getStatus());
        }

        System.out.print("Select attendance record (enter number): ");
        int attChoice = Integer.parseInt(scanner.nextLine().trim()) - 1;

        if (attChoice < 0 || attChoice >= attendanceList.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        Attendance attendance = attendanceList.get(attChoice);

        System.out.println("Select new status:");
        System.out.println("1. Present");
        System.out.println("2. Absent");
        System.out.println("3. Late");
        System.out.print("Enter choice: ");
        String statusChoice = scanner.nextLine().trim();

        String status;
        switch (statusChoice) {
            case "1":
                status = "Present";
                break;
            case "2":
                status = "Absent";
                break;
            case "3":
                status = "Late";
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        System.out.print("Enter remarks (optional): ");
        String remarks = scanner.nextLine().trim();

        attendanceService.updateAttendance(attendance.getId(), status, remarks.isEmpty() ? null : remarks);
        System.out.println("Attendance updated successfully!");
    }

    private void handleReportsMenu() {
        while (true) {
            System.out.println("\n===== REPORTS & ANALYTICS =====");
            System.out.println("1. Student Performance Report");
            System.out.println("2. Course Enrollment Statistics");
            System.out.println("3. Attendance Summary Report");
            System.out.println("4. Active Students List");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1":
                        studentPerformanceReport();
                        break;
                    case "2":
                        courseEnrollmentStatistics();
                        break;
                    case "3":
                        attendanceSummaryReport();
                        break;
                    case "4":
                        activeStudentsList();
                        break;
                    case "0":
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void studentPerformanceReport() throws DatabaseException, RecordNotFoundException {
        System.out.println("\n----- Student Performance Report -----");
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine().trim();

        Student student = studentService.getStudentByStudentId(studentId);
        List<Enrollment> enrollments = enrollmentService.getStudentEnrollments(student.getId());

        System.out.println("\n===== PERFORMANCE REPORT =====");
        System.out.println("Student: " + student.getFirstName() + " " + student.getLastName());
        System.out.println("Student ID: " + student.getStudentId());
        System.out.println("Email: " + student.getEmail());
        System.out.println("Status: " + student.getStatus());
        System.out.println("\nCourse Performance:");
        System.out.printf("%-10s %-30s %-10s %-15s %-15s%n",
                "Code", "Course Name", "Grade", "Status", "Attendance %");
        System.out.println("---------------------------------------------------------------------------------");

        for (Enrollment enrollment : enrollments) {
            double attendance = attendanceService.getAttendancePercentage(enrollment.getId());
            System.out.printf("%-10s %-30s %-10s %-15s %-15.2f%%%n",
                    enrollment.getCourse().getCourseCode(),
                    enrollment.getCourse().getCourseName(),
                    enrollment.getGrade() != null ? enrollment.getGrade() : "N/A",
                    enrollment.getStatus(),
                    attendance);
        }
    }

    private void courseEnrollmentStatistics() throws DatabaseException {
        System.out.println("\n----- Course Enrollment Statistics -----");
        List<Course> courses = courseService.getAllCourses();

        System.out.printf("%-10s %-30s %-12s %-12s %-15s%n",
                "Code", "Name", "Enrolled", "Capacity", "Availability");
        System.out.println("---------------------------------------------------------------------------------");

        for (Course course : courses) {
            int enrolled = enrollmentService.getCourseEnrollmentCount(course.getId());
            int available = course.getMaxCapacity() - enrolled;
            String availability = available > 0 ? "Available" : "Full";

            System.out.printf("%-10s %-30s %-12d %-12d %-15s%n",
                    course.getCourseCode(),
                    course.getCourseName(),
                    enrolled,
                    course.getMaxCapacity(),
                    availability);
        }
    }

    private void attendanceSummaryReport() throws DatabaseException {
        System.out.println("\n----- Attendance Summary Report -----");
        List<Student> students = studentService.getActiveStudents();

        System.out.printf("%-12s %-25s %-20s%n",
                "Student ID", "Name", "Avg Attendance %");
        System.out.println("--------------------------------------------------------");

        for (Student student : students) {
            List<Enrollment> enrollments = enrollmentService.getStudentEnrollments(student.getId());
            if (enrollments.isEmpty()) continue;

            double totalAttendance = 0;
            int courseCount = 0;

            for (Enrollment enrollment : enrollments) {
                if (enrollment.getStatus().equals("Enrolled") || enrollment.getStatus().equals("Completed")) {
                    totalAttendance += attendanceService.getAttendancePercentage(enrollment.getId());
                    courseCount++;
                }
            }

            double avgAttendance = courseCount > 0 ? totalAttendance / courseCount : 0;

            System.out.printf("%-12s %-25s %-20.2f%%%n",
                    student.getStudentId(),
                    student.getFirstName() + " " + student.getLastName(),
                    avgAttendance);
        }
    }

    private void activeStudentsList() throws DatabaseException {
        System.out.println("\n----- Active Students List -----");
        List<Student> students = studentService.getActiveStudents();

        if (students.isEmpty()) {
            System.out.println("No active students found.");
            return;
        }

        System.out.printf("%-12s %-25s %-30s %-15s %-15s%n",
                "Student ID", "Name", "Email", "Phone", "Enrollment Date");
        System.out.println("------------------------------------------------------------------------------------------------");

        for (Student student : students) {
            System.out.printf("%-12s %-25s %-30s %-15s %-15s%n",
                    student.getStudentId(),
                    student.getFirstName() + " " + student.getLastName(),
                    student.getEmail(),
                    student.getPhone(),
                    student.getEnrollmentDate());
        }

        System.out.println("\nTotal Active Students: " + students.size());
    }
}
