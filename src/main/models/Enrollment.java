package com.sms.models;

import java.time.LocalDate;
import java.util.UUID;

public class Enrollment {
    private UUID id;
    private UUID studentId;
    private UUID courseId;
    private LocalDate enrollmentDate;
    private String grade;
    private String status;
    private Student student;
    private Course course;

    public Enrollment() {}

    public Enrollment(UUID studentId, UUID courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.enrollmentDate = LocalDate.now();
        this.status = "Enrolled";
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getStudentId() {
        return studentId;
    }

    public void setStudentId(UUID studentId) {
        this.studentId = studentId;
    }

    public UUID getCourseId() {
        return courseId;
    }

    public void setCourseId(UUID courseId) {
        this.courseId = courseId;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return String.format("Enrollment[Student=%s, Course=%s, Status=%s, Grade=%s]",
                student != null ? student.getStudentId() : studentId,
                course != null ? course.getCourseCode() : courseId,
                status, grade != null ? grade : "N/A");
    }
}
