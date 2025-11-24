package com.sms.dao;

import com.sms.exceptions.DatabaseException;
import com.sms.exceptions.RecordNotFoundException;
import com.sms.models.Course;
import com.sms.utils.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CourseDAO {
    private static final Logger logger = LoggerFactory.getLogger(CourseDAO.class);

    public void create(Course course) throws DatabaseException {
        String sql = "INSERT INTO courses (course_code, course_name, description, credits, " +
                    "instructor, semester, max_capacity) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, course.getCourseCode());
            stmt.setString(2, course.getCourseName());
            stmt.setString(3, course.getDescription());
            stmt.setInt(4, course.getCredits());
            stmt.setString(5, course.getInstructor());
            stmt.setString(6, course.getSemester());
            stmt.setInt(7, course.getMaxCapacity());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Creating course failed, no rows affected");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    course.setId((UUID) generatedKeys.getObject(1));
                }
            }

            logger.info("Course created successfully: " + course.getCourseCode());
        } catch (SQLException e) {
            logger.error("Error creating course: " + e.getMessage());
            throw new DatabaseException("Failed to create course", e);
        }
    }

    public Course findById(UUID id) throws DatabaseException, RecordNotFoundException {
        String sql = "SELECT * FROM courses WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToCourse(rs);
            } else {
                throw new RecordNotFoundException("Course not found with id: " + id);
            }
        } catch (SQLException e) {
            logger.error("Error finding course by id: " + e.getMessage());
            throw new DatabaseException("Failed to find course", e);
        }
    }

    public Course findByCourseCode(String courseCode) throws DatabaseException, RecordNotFoundException {
        String sql = "SELECT * FROM courses WHERE course_code = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, courseCode);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToCourse(rs);
            } else {
                throw new RecordNotFoundException("Course not found with code: " + courseCode);
            }
        } catch (SQLException e) {
            logger.error("Error finding course by code: " + e.getMessage());
            throw new DatabaseException("Failed to find course", e);
        }
    }

    public List<Course> findAll() throws DatabaseException {
        String sql = "SELECT * FROM courses ORDER BY course_code";
        List<Course> courses = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                courses.add(mapResultSetToCourse(rs));
            }

            logger.info("Retrieved " + courses.size() + " courses");
            return courses;
        } catch (SQLException e) {
            logger.error("Error retrieving all courses: " + e.getMessage());
            throw new DatabaseException("Failed to retrieve courses", e);
        }
    }

    public void update(Course course) throws DatabaseException {
        String sql = "UPDATE courses SET course_name = ?, description = ?, credits = ?, " +
                    "instructor = ?, semester = ?, max_capacity = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, course.getCourseName());
            stmt.setString(2, course.getDescription());
            stmt.setInt(3, course.getCredits());
            stmt.setString(4, course.getInstructor());
            stmt.setString(5, course.getSemester());
            stmt.setInt(6, course.getMaxCapacity());
            stmt.setObject(7, course.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Updating course failed, no rows affected");
            }

            logger.info("Course updated successfully: " + course.getCourseCode());
        } catch (SQLException e) {
            logger.error("Error updating course: " + e.getMessage());
            throw new DatabaseException("Failed to update course", e);
        }
    }

    public void delete(UUID id) throws DatabaseException {
        String sql = "DELETE FROM courses WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new DatabaseException("Deleting course failed, no rows affected");
            }

            logger.info("Course deleted successfully with id: " + id);
        } catch (SQLException e) {
            logger.error("Error deleting course: " + e.getMessage());
            throw new DatabaseException("Failed to delete course", e);
        }
    }

    public List<Course> findBySemester(String semester) throws DatabaseException {
        String sql = "SELECT * FROM courses WHERE semester = ? ORDER BY course_code";
        List<Course> courses = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, semester);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                courses.add(mapResultSetToCourse(rs));
            }

            return courses;
        } catch (SQLException e) {
            logger.error("Error finding courses by semester: " + e.getMessage());
            throw new DatabaseException("Failed to find courses by semester", e);
        }
    }

    private Course mapResultSetToCourse(ResultSet rs) throws SQLException {
        Course course = new Course();
        course.setId((UUID) rs.getObject("id"));
        course.setCourseCode(rs.getString("course_code"));
        course.setCourseName(rs.getString("course_name"));
        course.setDescription(rs.getString("description"));
        course.setCredits(rs.getInt("credits"));
        course.setInstructor(rs.getString("instructor"));
        course.setSemester(rs.getString("semester"));
        course.setMaxCapacity(rs.getInt("max_capacity"));
        return course;
    }
}
