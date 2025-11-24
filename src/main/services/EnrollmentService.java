package com.sms.services;

import com.sms.dao.CourseDAO;
import com.sms.dao.EnrollmentDAO;
import com.sms.dao.StudentDAO;
import com.sms.exceptions.DatabaseException;
import com.sms.exceptions.RecordNotFoundException;
import com.sms.exceptions.ValidationException;
import com.sms.models.Course;
import com.sms.models.Enrollment;
import com.sms.models.Student;
import com.sms.utils.Validator;

import java.util.List;
import java.util.UUID;

public class EnrollmentService {
    private final EnrollmentDAO enrollmentDAO;
    private final StudentDAO studentDAO;
    private final CourseDAO courseDAO;

    public EnrollmentService() {
        this.enrollmentDAO = new EnrollmentDAO();
        this.studentDAO = new StudentDAO();
        this.courseDAO = new CourseDAO();
    }

    public void enrollStudent(UUID studentId, UUID courseId) throws DatabaseException, RecordNotFoundException, ValidationException {
        Student student = studentDAO.findById(studentId);
        if (!student.getStatus().equals("Active")) {
            throw new ValidationException("Only active students can be enrolled");
        }

        Course course = courseDAO.findById(courseId);
        int currentEnrollments = enrollmentDAO.getEnrollmentCount(courseId);
        if (currentEnrollments >= course.getMaxCapacity()) {
            throw new ValidationException("Course has reached maximum capacity");
        }

        Enrollment enrollment = new Enrollment(studentId, courseId);
        enrollmentDAO.create(enrollment);
    }

    public Enrollment getEnrollment(UUID id) throws DatabaseException, RecordNotFoundException {
        Enrollment enrollment = enrollmentDAO.findById(id);
        Student student = studentDAO.findById(enrollment.getStudentId());
        Course course = courseDAO.findById(enrollment.getCourseId());
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        return enrollment;
    }

    public List<Enrollment> getStudentEnrollments(UUID studentId) throws DatabaseException {
        List<Enrollment> enrollments = enrollmentDAO.findByStudentId(studentId);
        for (Enrollment enrollment : enrollments) {
            try {
                Student student = studentDAO.findById(enrollment.getStudentId());
                Course course = courseDAO.findById(enrollment.getCourseId());
                enrollment.setStudent(student);
                enrollment.setCourse(course);
            } catch (RecordNotFoundException e) {
            }
        }
        return enrollments;
    }

    public List<Enrollment> getCourseEnrollments(UUID courseId) throws DatabaseException {
        List<Enrollment> enrollments = enrollmentDAO.findByCourseId(courseId);
        for (Enrollment enrollment : enrollments) {
            try {
                Student student = studentDAO.findById(enrollment.getStudentId());
                Course course = courseDAO.findById(enrollment.getCourseId());
                enrollment.setStudent(student);
                enrollment.setCourse(course);
            } catch (RecordNotFoundException e) {
            }
        }
        return enrollments;
    }

    public void updateGrade(UUID enrollmentId, String grade) throws DatabaseException, RecordNotFoundException, ValidationException {
        Validator.validateGrade(grade);
        Enrollment enrollment = enrollmentDAO.findById(enrollmentId);
        enrollment.setGrade(grade);
        enrollmentDAO.update(enrollment);
    }

    public void dropEnrollment(UUID enrollmentId) throws DatabaseException, RecordNotFoundException {
        Enrollment enrollment = enrollmentDAO.findById(enrollmentId);
        enrollment.setStatus("Dropped");
        enrollmentDAO.update(enrollment);
    }

    public void completeEnrollment(UUID enrollmentId, String grade) throws DatabaseException, RecordNotFoundException, ValidationException {
        Validator.validateGrade(grade);
        Enrollment enrollment = enrollmentDAO.findById(enrollmentId);
        enrollment.setGrade(grade);
        enrollment.setStatus("Completed");
        enrollmentDAO.update(enrollment);
    }

    public int getCourseEnrollmentCount(UUID courseId) throws DatabaseException {
        return enrollmentDAO.getEnrollmentCount(courseId);
    }
}
