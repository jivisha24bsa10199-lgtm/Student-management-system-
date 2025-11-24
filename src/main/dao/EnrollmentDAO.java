package com.sms.dao;

import com.sms.exceptions.DatabaseException;
import com.sms.exceptions.RecordNotFoundException;
import com.sms.models.Enrollment;
import com.sms.utils.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EnrollmentDAO {
    private static final Logger logger = LoggerFactory.getLogger(EnrollmentDAO.class);

    public void create(Enrollment enrollment) throws DatabaseException {
        String sql = "INSERT INTO enrollments (student_id, course_id, enrollment_date, status) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setObject(1, enrollment.getStudentId());
            stmt.setObject(2, enrollment.getCourseId());
            stmt.setDate(3, Date.valueOf(enrollment.getEnrollmentDate()));
            stmt.setString(4, enrollment.getStatus());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Creating enrollment failed, no rows affected");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    enrollment.setId((UUID) generatedKeys.getObject(1));
                }
            }

            logger.info("Enrollment created successfully");
        } catch (SQLException e) {
            logger.error("Error creating enrollment: " + e.getMessage());
            throw new DatabaseException("Failed to create enrollment", e);
        }
    }

    public Enrollment findById(UUID id) throws DatabaseException, RecordNotFoundException {
        String sql = "SELECT * FROM enrollments WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToEnrollment(rs);
            } else {
                throw new RecordNotFoundException("Enrollment not found with id: " + id);
            }
        } catch (SQLException e) {
            logger.error("Error finding enrollment by id: " + e.getMessage());
            throw new DatabaseException("Failed to find enrollment", e);
        }
    }

    public List<Enrollment> findByStudentId(UUID studentId) throws DatabaseException {
        String sql = "SELECT * FROM enrollments WHERE student_id = ? ORDER BY enrollment_date DESC";
        List<Enrollment> enrollments = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, studentId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                enrollments.add(mapResultSetToEnrollment(rs));
            }

            return enrollments;
        } catch (SQLException e) {
            logger.error("Error finding enrollments by student: " + e.getMessage());
            throw new DatabaseException("Failed to find enrollments by student", e);
        }
    }

    public List<Enrollment> findByCourseId(UUID courseId) throws DatabaseException {
        String sql = "SELECT * FROM enrollments WHERE course_id = ? ORDER BY enrollment_date DESC";
        List<Enrollment> enrollments = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, courseId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                enrollments.add(mapResultSetToEnrollment(rs));
            }

            return enrollments;
        } catch (SQLException e) {
            logger.error("Error finding enrollments by course: " + e.getMessage());
            throw new DatabaseException("Failed to find enrollments by course", e);
        }
    }

    public List<Enrollment> findAll() throws DatabaseException {
        String sql = "SELECT * FROM enrollments ORDER BY enrollment_date DESC";
        List<Enrollment> enrollments = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                enrollments.add(mapResultSetToEnrollment(rs));
            }

            logger.info("Retrieved " + enrollments.size() + " enrollments");
            return enrollments;
        } catch (SQLException e) {
            logger.error("Error retrieving all enrollments: " + e.getMessage());
            throw new DatabaseException("Failed to retrieve enrollments", e);
        }
    }

    public void update(Enrollment enrollment) throws DatabaseException {
        String sql = "UPDATE enrollments SET grade = ?, status = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, enrollment.getGrade());
            stmt.setString(2, enrollment.getStatus());
            stmt.setObject(3, enrollment.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Updating enrollment failed, no rows affected");
            }

            logger.info("Enrollment updated successfully");
        } catch (SQLException e) {
            logger.error("Error updating enrollment: " + e.getMessage());
            throw new DatabaseException("Failed to update enrollment", e);
        }
    }

    public void delete(UUID id) throws DatabaseException {
        String sql = "DELETE FROM enrollments WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new DatabaseException("Deleting enrollment failed, no rows affected");
            }

            logger.info("Enrollment deleted successfully with id: " + id);
        } catch (SQLException e) {
            logger.error("Error deleting enrollment: " + e.getMessage());
            throw new DatabaseException("Failed to delete enrollment", e);
        }
    }

    public int getEnrollmentCount(UUID courseId) throws DatabaseException {
        String sql = "SELECT COUNT(*) as count FROM enrollments WHERE course_id = ? AND status = 'Enrolled'";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, courseId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("count");
            }
            return 0;
        } catch (SQLException e) {
            logger.error("Error getting enrollment count: " + e.getMessage());
            throw new DatabaseException("Failed to get enrollment count", e);
        }
    }

    private Enrollment mapResultSetToEnrollment(ResultSet rs) throws SQLException {
        Enrollment enrollment = new Enrollment();
        enrollment.setId((UUID) rs.getObject("id"));
        enrollment.setStudentId((UUID) rs.getObject("student_id"));
        enrollment.setCourseId((UUID) rs.getObject("course_id"));
        enrollment.setEnrollmentDate(rs.getDate("enrollment_date").toLocalDate());
        enrollment.setGrade(rs.getString("grade"));
        enrollment.setStatus(rs.getString("status"));
        return enrollment;
    }
}
