package com.sms.utils;

import com.sms.exceptions.ValidationException;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class Validator {
    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern PHONE_PATTERN =
        Pattern.compile("^[0-9]{10}$");

    private static final Pattern STUDENT_ID_PATTERN =
        Pattern.compile("^STU[0-9]{3,}$");

    private static final Pattern COURSE_CODE_PATTERN =
        Pattern.compile("^[A-Z]{2,4}[0-9]{3}$");

    public static void validateEmail(String email) throws ValidationException {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("Email cannot be empty");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new ValidationException("Invalid email format");
        }
    }

    public static void validatePhone(String phone) throws ValidationException {
        if (phone != null && !phone.trim().isEmpty()) {
            if (!PHONE_PATTERN.matcher(phone).matches()) {
                throw new ValidationException("Phone number must be 10 digits");
            }
        }
    }

    public static void validateStudentId(String studentId) throws ValidationException {
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new ValidationException("Student ID cannot be empty");
        }
        if (!STUDENT_ID_PATTERN.matcher(studentId).matches()) {
            throw new ValidationException("Student ID must be in format STU### (e.g., STU001)");
        }
    }

    public static void validateCourseCode(String courseCode) throws ValidationException {
        if (courseCode == null || courseCode.trim().isEmpty()) {
            throw new ValidationException("Course code cannot be empty");
        }
        if (!COURSE_CODE_PATTERN.matcher(courseCode).matches()) {
            throw new ValidationException("Course code must be in format XX### (e.g., CS101)");
        }
    }

    public static void validateName(String name, String fieldName) throws ValidationException {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException(fieldName + " cannot be empty");
        }
        if (name.length() < 2) {
            throw new ValidationException(fieldName + " must be at least 2 characters long");
        }
    }

    public static void validateDateOfBirth(LocalDate dateOfBirth) throws ValidationException {
        if (dateOfBirth == null) {
            throw new ValidationException("Date of birth cannot be empty");
        }
        if (dateOfBirth.isAfter(LocalDate.now())) {
            throw new ValidationException("Date of birth cannot be in the future");
        }
        if (dateOfBirth.isBefore(LocalDate.now().minusYears(100))) {
            throw new ValidationException("Date of birth is too old");
        }
    }

    public static void validateCredits(int credits) throws ValidationException {
        if (credits < 1 || credits > 6) {
            throw new ValidationException("Credits must be between 1 and 6");
        }
    }

    public static void validateCapacity(int capacity) throws ValidationException {
        if (capacity < 1 || capacity > 500) {
            throw new ValidationException("Capacity must be between 1 and 500");
        }
    }

    public static void validateGrade(String grade) throws ValidationException {
        if (grade != null && !grade.trim().isEmpty()) {
            String[] validGrades = {"A+", "A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D", "F"};
            boolean isValid = false;
            for (String validGrade : validGrades) {
                if (validGrade.equals(grade)) {
                    isValid = true;
                    break;
                }
            }
            if (!isValid) {
                throw new ValidationException("Invalid grade. Must be one of: A+, A, A-, B+, B, B-, C+, C, C-, D, F");
            }
        }
    }
}
