package com.sms.services;

import com.sms.dao.CourseDAO;
import com.sms.exceptions.DatabaseException;
import com.sms.exceptions.RecordNotFoundException;
import com.sms.exceptions.ValidationException;
import com.sms.models.Course;
import com.sms.utils.Validator;

import java.util.List;
import java.util.UUID;

public class CourseService {
    private final CourseDAO courseDAO;

    public CourseService() {
        this.courseDAO = new CourseDAO();
    }

    public void addCourse(Course course) throws ValidationException, DatabaseException {
        Validator.validateCourseCode(course.getCourseCode());
        Validator.validateName(course.getCourseName(), "Course name");
        Validator.validateCredits(course.getCredits());
        Validator.validateCapacity(course.getMaxCapacity());

        courseDAO.create(course);
    }

    public Course getCourse(UUID id) throws DatabaseException, RecordNotFoundException {
        return courseDAO.findById(id);
    }

    public Course getCourseByCourseCode(String courseCode) throws DatabaseException, RecordNotFoundException {
        return courseDAO.findByCourseCode(courseCode);
    }

    public List<Course> getAllCourses() throws DatabaseException {
        return courseDAO.findAll();
    }

    public void updateCourse(Course course) throws ValidationException, DatabaseException {
        Validator.validateName(course.getCourseName(), "Course name");
        Validator.validateCredits(course.getCredits());
        Validator.validateCapacity(course.getMaxCapacity());

        courseDAO.update(course);
    }

    public void deleteCourse(UUID id) throws DatabaseException {
        courseDAO.delete(id);
    }

    public List<Course> getCoursesBySemester(String semester) throws DatabaseException {
        return courseDAO.findBySemester(semester);
    }
}
