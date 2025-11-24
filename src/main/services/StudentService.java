package com.sms.services;

import com.sms.dao.StudentDAO;
import com.sms.exceptions.DatabaseException;
import com.sms.exceptions.RecordNotFoundException;
import com.sms.exceptions.ValidationException;
import com.sms.models.Student;
import com.sms.utils.Validator;

import java.util.List;
import java.util.UUID;

public class StudentService {
    private final StudentDAO studentDAO;

    public StudentService() {
        this.studentDAO = new StudentDAO();
    }

    public void addStudent(Student student) throws ValidationException, DatabaseException {
        Validator.validateStudentId(student.getStudentId());
        Validator.validateName(student.getFirstName(), "First name");
        Validator.validateName(student.getLastName(), "Last name");
        Validator.validateEmail(student.getEmail());
        Validator.validatePhone(student.getPhone());
        Validator.validateDateOfBirth(student.getDateOfBirth());

        studentDAO.create(student);
    }

    public Student getStudent(UUID id) throws DatabaseException, RecordNotFoundException {
        return studentDAO.findById(id);
    }

    public Student getStudentByStudentId(String studentId) throws DatabaseException, RecordNotFoundException {
        return studentDAO.findByStudentId(studentId);
    }

    public List<Student> getAllStudents() throws DatabaseException {
        return studentDAO.findAll();
    }

    public void updateStudent(Student student) throws ValidationException, DatabaseException {
        Validator.validateName(student.getFirstName(), "First name");
        Validator.validateName(student.getLastName(), "Last name");
        Validator.validateEmail(student.getEmail());
        Validator.validatePhone(student.getPhone());
        Validator.validateDateOfBirth(student.getDateOfBirth());

        studentDAO.update(student);
    }

    public void deleteStudent(UUID id) throws DatabaseException {
        studentDAO.delete(id);
    }

    public List<Student> getActiveStudents() throws DatabaseException {
        return studentDAO.findByStatus("Active");
    }

    public void changeStudentStatus(UUID id, String status) throws DatabaseException, RecordNotFoundException, ValidationException {
        Student student = studentDAO.findById(id);
        if (!status.equals("Active") && !status.equals("Inactive") && !status.equals("Graduated")) {
            throw new ValidationException("Status must be Active, Inactive, or Graduated");
        }
        student.setStatus(status);
        studentDAO.update(student);
    }
}
