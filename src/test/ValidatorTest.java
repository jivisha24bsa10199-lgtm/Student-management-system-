package com.sms.utils;

import com.sms.exceptions.ValidationException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ValidatorTest {

    @Test
    public void testValidEmail() {
        assertDoesNotThrow(() -> Validator.validateEmail("student@example.com"));
        assertDoesNotThrow(() -> Validator.validateEmail("john.doe@university.edu"));
    }

    @Test
    public void testInvalidEmail() {
        assertThrows(ValidationException.class, () -> Validator.validateEmail("invalid-email"));
        assertThrows(ValidationException.class, () -> Validator.validateEmail(""));
        assertThrows(ValidationException.class, () -> Validator.validateEmail(null));
    }

    @Test
    public void testValidPhone() {
        assertDoesNotThrow(() -> Validator.validatePhone("1234567890"));
        assertDoesNotThrow(() -> Validator.validatePhone(null));
        assertDoesNotThrow(() -> Validator.validatePhone(""));
    }

    @Test
    public void testInvalidPhone() {
        assertThrows(ValidationException.class, () -> Validator.validatePhone("123"));
        assertThrows(ValidationException.class, () -> Validator.validatePhone("abcdefghij"));
    }

    @Test
    public void testValidStudentId() {
        assertDoesNotThrow(() -> Validator.validateStudentId("STU001"));
        assertDoesNotThrow(() -> Validator.validateStudentId("STU12345"));
    }

    @Test
    public void testInvalidStudentId() {
        assertThrows(ValidationException.class, () -> Validator.validateStudentId("STU1"));
        assertThrows(ValidationException.class, () -> Validator.validateStudentId("ABC001"));
        assertThrows(ValidationException.class, () -> Validator.validateStudentId(""));
    }

    @Test
    public void testValidCourseCode() {
        assertDoesNotThrow(() -> Validator.validateCourseCode("CS101"));
        assertDoesNotThrow(() -> Validator.validateCourseCode("MATH201"));
    }

    @Test
    public void testInvalidCourseCode() {
        assertThrows(ValidationException.class, () -> Validator.validateCourseCode("C101"));
        assertThrows(ValidationException.class, () -> Validator.validateCourseCode("CS10"));
        assertThrows(ValidationException.class, () -> Validator.validateCourseCode(""));
    }

    @Test
    public void testValidName() {
        assertDoesNotThrow(() -> Validator.validateName("John", "First name"));
        assertDoesNotThrow(() -> Validator.validateName("Mary Jane", "Name"));
    }

    @Test
    public void testInvalidName() {
        assertThrows(ValidationException.class, () -> Validator.validateName("J", "First name"));
        assertThrows(ValidationException.class, () -> Validator.validateName("", "First name"));
        assertThrows(ValidationException.class, () -> Validator.validateName(null, "First name"));
    }

    @Test
    public void testValidDateOfBirth() {
        assertDoesNotThrow(() -> Validator.validateDateOfBirth(LocalDate.of(2000, 1, 1)));
        assertDoesNotThrow(() -> Validator.validateDateOfBirth(LocalDate.now().minusYears(20)));
    }

    @Test
    public void testInvalidDateOfBirth() {
        assertThrows(ValidationException.class, () -> Validator.validateDateOfBirth(null));
        assertThrows(ValidationException.class, () -> Validator.validateDateOfBirth(LocalDate.now().plusDays(1)));
        assertThrows(ValidationException.class, () -> Validator.validateDateOfBirth(LocalDate.now().minusYears(101)));
    }

    @Test
    public void testValidCredits() {
        assertDoesNotThrow(() -> Validator.validateCredits(3));
        assertDoesNotThrow(() -> Validator.validateCredits(1));
        assertDoesNotThrow(() -> Validator.validateCredits(6));
    }

    @Test
    public void testInvalidCredits() {
        assertThrows(ValidationException.class, () -> Validator.validateCredits(0));
        assertThrows(ValidationException.class, () -> Validator.validateCredits(7));
        assertThrows(ValidationException.class, () -> Validator.validateCredits(-1));
    }

    @Test
    public void testValidCapacity() {
        assertDoesNotThrow(() -> Validator.validateCapacity(30));
        assertDoesNotThrow(() -> Validator.validateCapacity(1));
        assertDoesNotThrow(() -> Validator.validateCapacity(500));
    }

    @Test
    public void testInvalidCapacity() {
        assertThrows(ValidationException.class, () -> Validator.validateCapacity(0));
        assertThrows(ValidationException.class, () -> Validator.validateCapacity(501));
        assertThrows(ValidationException.class, () -> Validator.validateCapacity(-10));
    }

    @Test
    public void testValidGrade() {
        assertDoesNotThrow(() -> Validator.validateGrade("A"));
        assertDoesNotThrow(() -> Validator.validateGrade("B+"));
        assertDoesNotThrow(() -> Validator.validateGrade("C-"));
        assertDoesNotThrow(() -> Validator.validateGrade(null));
        assertDoesNotThrow(() -> Validator.validateGrade(""));
    }

    @Test
    public void testInvalidGrade() {
        assertThrows(ValidationException.class, () -> Validator.validateGrade("A++"));
        assertThrows(ValidationException.class, () -> Validator.validateGrade("E"));
        assertThrows(ValidationException.class, () -> Validator.validateGrade("X"));
    }
}
